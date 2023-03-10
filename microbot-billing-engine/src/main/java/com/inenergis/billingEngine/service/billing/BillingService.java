package com.inenergis.billingEngine.service.billing;

import com.inenergis.billingEngine.exception.InvoiceCalculationException;
import com.inenergis.billingEngine.service.ServiceAgreementService;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.BaseServicePoint;
import com.inenergis.entity.billing.BillingException;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.billing.Payment;
import com.inenergis.entity.genericEnum.PaymentType;
import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import com.inenergis.entity.genericEnum.RateEventFee;
import com.inenergis.entity.genericEnum.TierBoundType;
import com.inenergis.entity.genericEnum.TierOperatorType;
import com.inenergis.entity.genericEnum.TierPenaltyPeriod;
import com.inenergis.entity.genericEnum.TierVariableType;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.meterData.IntervalData;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import com.inenergis.billingEngine.service.BaselineAllowanceService;
import com.inenergis.billingEngine.service.ConsumptionService;
import com.inenergis.billingEngine.service.ElasticClientService;
import com.inenergis.billingEngine.service.InvoiceService;
import com.inenergis.billingEngine.service.MeterService;
import com.inenergis.billingEngine.service.PaymentService;
import com.inenergis.billingEngine.service.TimeOfUseCalendarService;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.inenergis.entity.billing.Invoice.InvoiceStatus.FINAL;
import static com.inenergis.entity.billing.Invoice.InvoiceStatus.WITH_ERRORS;

@Component
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    @Autowired
    private TimeOfUseCalendarService timeOfUseCalendarService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MeterService meterService;

    @Autowired
    private ServiceAgreementService serviceAgreementService;

    @Autowired
    private BaselineAllowanceService baselineAllowanceService;

    @Autowired
    private ConsumptionService consumptionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AncillaryCalculationsService ancillaryCalculationsService;
    @Autowired
    private CommonBillingService commonBillingService;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void generateInvoice(String serviceAgrementId, Invoice invoice) throws IOException {
        log.info("Generating invoice for service agreement {}",  serviceAgrementId);
        BaseServiceAgreement serviceAgreement = serviceAgreementService.getServiceAgreement(serviceAgrementId);
        if (serviceAgreement == null || CollectionUtils.isEmpty(serviceAgreement.getRatePlanEnrollments())) {
            log.info("service agreement {} has no rate plan enrollments", serviceAgrementId);
            return;
        }
        for (RatePlanEnrollment ratePlanEnrollment : serviceAgreement.getRatePlanEnrollments()) {
            if (ratePlanEnrollment.getRatePlan().getActiveProfile() == null || ! ratePlanEnrollment.isActive()){
                continue;
            }
            List<InvoiceLine> invoiceLines = new ArrayList<>();
            List<BillingException> billingExceptions = new ArrayList<>();
            for (AgreementPointMap agreementPointMap : serviceAgreement.getAgreementPointMaps()) {
                try{
                    Map<LocalDate, RateTier> localDateRateTierMap = calculateConsumptionFees(invoice, serviceAgreement, ratePlanEnrollment.getRatePlan().getActiveProfile(), invoiceLines, billingExceptions, agreementPointMap);
                    ancillaryCalculationsService.calculateAncillaryFees( agreementPointMap.getServicePoint(), ratePlanEnrollment.getRatePlan().getActiveProfile(), invoice, invoiceLines, billingExceptions, localDateRateTierMap);
                } catch (Exception e){
                    log.error("Unexpected error generating invoice",e);
                    billingExceptions.add(BillingException.builder().invoice(invoice).message(e.getMessage()).build());
                }
            }
            clearInvoiceCollections(invoice);
            invoice.getInvoiceLines().addAll(invoiceLines.stream().filter(line -> line.getTotal()!=0).collect(Collectors.toList()));
            invoice.getExceptions().addAll(billingExceptions);
        }
        if(invoice.shouldBeSaved()){
            log.info("inside if");
            try {
                invoice.setStatus(invoice.getExceptions().isEmpty()? FINAL: WITH_ERRORS);
                invoiceService.saveInvoice(invoice);
            } catch (IOException e) {
                log.warn(ElasticClientService.INVOICE_MODIFICATIONS_POPULATION_TO_ELASTIC_SEARCH_FAILED,e);
            }
            //TODO remove this, is for test
            log.info("Finished with an invoice with invoice Id {} :::", invoice.getId());
            submitChargesToUtility(invoice.getId());
            //new Thread(() -> submitChargesToUtility(invoice.getId())).start();
            log.info("Finished with an invoice with {} lines", invoice.getInvoiceNumber());
            new Thread(() -> receivePayments(invoice.getId())).start();
        }
        log.info("Finished with an invoice with {} lines", invoice.getDescription());
        log.info("Finished with an invoice with {} lines", invoice.getInvoiceNumber());
        log.info("Finished with an invoice with {} lines", invoice.getTotalConsumption());
        log.info("Finished with an invoice with {} lines", invoice.getInvoiceLines().size());

    }

    public void clearInvoiceCollections(Invoice invoice) {
        if (invoice.getInvoiceLines() != null) {
            invoice.getInvoiceLines().clear();
        } else {
            invoice.setInvoiceLines(new ArrayList<>());
        }
        if (invoice.getExceptions() != null) {
            invoice.getExceptions().clear();
        } else {
            invoice.setExceptions(new ArrayList<>());
        }

    }

    public Map<LocalDate, RateTier> calculateConsumptionFees(Invoice invoice, BaseServiceAgreement serviceAgreement, RatePlanProfile rateProfile, List<InvoiceLine> invoiceLines, List<BillingException> billingExceptions, AgreementPointMap agreementPointMap) {
        Map<LocalDate, RateTier> result = new HashMap<>();
        List<IntervalData> meterDataUsage = meterService.getMeterDataUsage(agreementPointMap.getServicePoint().getServicePointId(), invoice.getDateFrom(), invoice.getDateTo());

        log.info("AgreementPoint {}",  agreementPointMap.getServicePoint().getServicePointId());
        log.info("Date From {}",  invoice.getDateFrom());
        log.info("Date To {}",  invoice.getDateTo());


        log.info("Meter data usage {}",  meterDataUsage);
        // TODO in different days we can have different rates for the user

        List<RateProfileConsumptionFee> consumptionFees = rateProfile.getActiveConsumptionFees();

        log.info("Rate Profile  Consumption fee {}",  consumptionFees);
        Map<RateTier, List<RateProfileConsumptionFee>> feesByTier = consumptionFees.stream().collect(Collectors.groupingBy(RateProfileConsumptionFee::getRateTier));
        Map<RateProfileConsumptionFee, BigDecimal> consumptionsByFee = consumptionFees.stream().collect(Collectors.toMap(fee -> fee, fee -> BigDecimal.ZERO,(v1,v2)->v1));

        if (CollectionUtils.isEmpty(meterDataUsage)) {
            return populateAllDays(result, invoice, feesByTier.keySet());
        }

        BigDecimal accumulatedConsumption = BigDecimal.ZERO;
        TimeOfUseCalendar latestCalendar = null;
        log.info("Before Processing "+LocalDateTime.now()+ "meterdataUsage size "+ meterDataUsage.size());
        String latestDate = null;
        for (IntervalData meterData : meterDataUsage) {
            TimeOfUseCalendar calendar = latestCalendar;
            LocalDate meterDate = LocalDate.parse(meterData.getDate(), dayFormatter);
            if(latestDate ==null || !latestDate.equals(meterData.getDate())){
                calendar = commonBillingService.getCalendarByDate(calendar, meterDate);
                result.put(meterDate, null);
            }
            latestDate=meterData.getDate();
            if(latestCalendar != null && !latestCalendar.equals(calendar)){
                accumulatedConsumption = BigDecimal.ZERO;
            }
            latestCalendar = calendar;
            accumulatedConsumption = accumulatedConsumption.add(BigDecimal.ZERO.max(meterData.getValue()));
            log.info("accumulatedConsumption", accumulatedConsumption);
            BigDecimal highestConsumption = consumptionService.calculateHighestConsumption(meterDataUsage, calendar);

            try{
                RateTier rateTier = processMeterData(rateProfile, meterData, feesByTier, highestConsumption, accumulatedConsumption, consumptionsByFee, agreementPointMap.getServicePoint(), calendar, invoice, serviceAgreement);
                log.info(" highest: " + highestConsumption + " accumulated: "+accumulatedConsumption );
                if(result.get(meterDate) == null || result.get(meterDate).getId() < rateTier.getId()){ // TODO introduce ordering...
                    result.put(meterDate, rateTier);
                }
            } catch (InvoiceCalculationException e) {
                billingExceptions.add(BillingException.builder().invoice(invoice).message(e.getMessage()).build());
            }
        }
        for (Map.Entry<RateProfileConsumptionFee, BigDecimal> entry : consumptionsByFee.entrySet()) {
            invoiceLines.add(commonBillingService.generateInvoiceLine(entry.getKey(), commonBillingService.generateInvoiceLineConcept(entry.getKey()), entry.getValue(), invoice, agreementPointMap.getServicePoint()));
        }
        return populateAllDays(result, invoice, feesByTier.keySet());
    }

    private Map<LocalDate, RateTier> populateAllDays(Map<LocalDate, RateTier> result, Invoice invoice, Set<RateTier> rateTiers) {
        if (!rateTiers.isEmpty()) {
            RateTier rateTier = rateTiers.stream().sorted(Comparator.comparing(RateTier::getId)).findFirst().get();
            LocalDate currentDate = invoice.getDateFrom();
            while (!invoice.getDateTo().isBefore(currentDate)) {
                if (!result.containsKey(currentDate) || result.get(currentDate) == null){
                    result.put(currentDate, rateTier);
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        return result;
    }

    public RateTier processMeterData(RatePlanProfile rateProfile, IntervalData meterData, Map<RateTier, List<RateProfileConsumptionFee>> feesByTier, BigDecimal highestConsumption, BigDecimal accumulatedConsumption,
                                 Map<RateProfileConsumptionFee, BigDecimal> consumptionsByFee, BaseServicePoint servicePoint, TimeOfUseCalendar calendar, Invoice invoice, BaseServiceAgreement serviceAgreement) throws InvoiceCalculationException {

        if (meterData == null) {
            throw new InvoiceCalculationException("MeterData must not be null");
        } else if (meterData.getValue() == null) {
            throw new InvoiceCalculationException("MeterData value must be specified");
        } else if (meterData.getTime() == null) {
            throw new InvoiceCalculationException("MeterData date must be specified");
        }

        LocalDateTime meterReadingDateTime = LocalDateTime.parse(meterData.getTime(), formatter);
        TimeOfUse timeOfUse = timeOfUseCalendarService.getTimeOfUse(calendar, meterReadingDateTime);
        Map<RateTier, BigDecimal> rateTierTotalMap = calculateTier(rateProfile, feesByTier.keySet(), highestConsumption, accumulatedConsumption, servicePoint, meterData.getValue(), calendar, invoice);
        //TODO is this UTC or California Time?
        RateEventFee rateEventFee = findRateEventFee(meterReadingDateTime, serviceAgreement);
        log.info("rateTierTotalMap: "+rateTierTotalMap);
        for (Map.Entry<RateTier, BigDecimal> tierValueEntry : rateTierTotalMap.entrySet()) {
            Map<String, List<RateProfileConsumptionFee>> feesByName = feesByTier.get(tierValueEntry.getKey()).stream().collect(Collectors.groupingBy(RateProfileConsumptionFee::getName));

            for (Map.Entry<String, List<RateProfileConsumptionFee>> entry : feesByName.entrySet()) {
                RateProfileConsumptionFee moreRestrictiveFee = getMoreRestrictive(entry.getValue(), calendar, timeOfUse, meterData.getType(), rateEventFee);
                if (moreRestrictiveFee != null) {
                    consumptionsByFee.put(moreRestrictiveFee,consumptionsByFee.get(moreRestrictiveFee).add(tierValueEntry.getValue()));
                }
            }
        }
        return commonBillingService.getHighestTier(rateTierTotalMap.keySet());
    }

    public Map<RateTier, BigDecimal> calculateTier(RatePlanProfile rateProfile, Set<RateTier> rateTiers, BigDecimal highestConsumption, BigDecimal accumulatedConsumption, BaseServicePoint servicePoint, BigDecimal value, TimeOfUseCalendar calendar, Invoice invoice) throws InvoiceCalculationException {
        BigDecimal valueForComparison = BigDecimal.ZERO;
        BigDecimal previousConsumption = BigDecimal.ZERO;
        LinkedHashMap<RateTier,BigDecimal> result = new LinkedHashMap<>();
        switch (rateProfile.getTierType()){
            case DEMAND:
                result.put(findTier(highestConsumption, rateTiers, servicePoint, calendar, invoice), value);
                return result;
            case USAGE:
                valueForComparison = accumulatedConsumption;
                previousConsumption = valueForComparison.subtract(value);
                break;
        }
        BigDecimal fromValue = BigDecimal.ZERO;
        BigDecimal previousBand = BigDecimal.ZERO;
        for (RateTier rateTier : rateTiers.stream().sorted(Comparator.comparing(IdentifiableEntity::getId)).collect(Collectors.toList())) {
            if (rateTier.getToBound().equals(TierBoundType.UNLIMITED)){
                result.put(rateTier,valueForComparison.subtract(fromValue.max(previousConsumption))); /// 203 - max(200, 98)
                return result;
            }
            BigDecimal toValue = calculateBoundValue(rateTier.getToBound(), rateTier.getToAmountValue(), rateTier.getToFormulaVariable(), rateTier.getToFormulaOperator(), servicePoint, calendar, invoice, previousBand);
            if(valueForComparison.compareTo(toValue)<=0){ // valueForComparison < toValue    203 < 200
                result.put(rateTier,valueForComparison.subtract(previousConsumption.max(fromValue)));
                return result;
            }else if(previousConsumption.compareTo(toValue)<=0){// previousConsumption < toValue    98 < 200
                result.put(rateTier,toValue.subtract(previousConsumption.max(fromValue))); // 200 - max(98,100) = 100
                fromValue = toValue;
            }
            previousBand = toValue;
        }
        return result ;
    }

    private RateTier findTier(BigDecimal highestConsumption, Set<RateTier> rateTiers, BaseServicePoint servicePoint, TimeOfUseCalendar calendar, Invoice invoice) throws InvoiceCalculationException {
        BigDecimal previousBand = BigDecimal.ZERO;
        for (RateTier rateTier : rateTiers.stream().collect(Collectors.toList())) {
            final BigDecimal fromBound = calculateBoundValue(rateTier.getFromBound(), rateTier.getFromAmountValue(), rateTier.getFromFormulaVariable(), rateTier.getFromFormulaOperator(), servicePoint, calendar, invoice, previousBand);
            if(fromBound.compareTo(highestConsumption) <= 0){
                if (rateTier.getToBound().equals(TierBoundType.UNLIMITED)){
                    return rateTier;
                }
                BigDecimal toBound = calculateBoundValue(rateTier.getToBound(), rateTier.getToAmountValue(), rateTier.getToFormulaVariable(), rateTier.getToFormulaOperator(), servicePoint, calendar, invoice, previousBand);
                if(toBound.compareTo(highestConsumption) > 0){
                    return rateTier;
                }
                previousBand = fromBound;
            }
        }
        throw new InvoiceCalculationException("No rate tier with unlimited");
    }

    private BigDecimal calculateBoundValue(TierBoundType bound, BigDecimal amountValue, TierVariableType formulaVariable, TierOperatorType formulaOperator, BaseServicePoint servicePoint, TimeOfUseCalendar calendar, Invoice invoice, BigDecimal previousBand) throws InvoiceCalculationException {
        if (bound.equals(TierBoundType.AMOUNT)) {
            return amountValue;
        } else if(bound.equals(TierBoundType.FORMULA)){
            return calculateBoundFormula(formulaVariable, formulaOperator, amountValue, servicePoint, calendar, invoice);
        } else if (bound.equals(TierBoundType.PREVIOUS_BAND)){
            return previousBand;
        }
        return null;
    }

    private BigDecimal calculateBoundFormula(TierVariableType formulaVariable, TierOperatorType formulaOperator, BigDecimal amountValue, BaseServicePoint servicePoint, TimeOfUseCalendar calendar, Invoice invoice) throws InvoiceCalculationException {
        BigDecimal calculatedValue = BigDecimal.ZERO;
        if(formulaVariable.equals(TierVariableType.BASELINE_ALLOWANCE)){
            BaselineAllowance baselineAllowance = baselineAllowanceService.findByTimeOfUseCalendarAndCode(calendar, servicePoint.getPremise().getBaseLineChar());
            if (baselineAllowance == null) {
                throw new InvoiceCalculationException(servicePoint.getPremise().getBaseLineChar() + " has no baseline allowance or is not correctly defined");
            }
            LocalDate from = invoice.getDateFrom();
            LocalDate to = invoice.getDateTo();
            if(calendar.getDateFrom().isAfter(from)){
                from = calendar.getDateFrom();
            }
            if (calendar.getDateTo().isBefore(to)) {
                to = calendar.getDateTo();
            }
            calculatedValue = baselineAllowance.getKwhPerDay().multiply(new BigDecimal(ChronoUnit.DAYS.between(from, to) + 1));
        }
        if(formulaOperator != null && amountValue != null){
            switch (formulaOperator){
                case PLUS:
                    calculatedValue = calculatedValue.add(amountValue);
                    break;
                case MINUS:
                    calculatedValue = calculatedValue.subtract(amountValue);
                    break;
                case MULTIPLY:
                    calculatedValue = calculatedValue.multiply(amountValue);
                    break;
                case DIVIDE:
                    calculatedValue = calculatedValue.divide(amountValue);
                    break;
            }
        }
        return calculatedValue;
    }

    private RateProfileConsumptionFee getMoreRestrictive(List<RateProfileConsumptionFee> fees, TimeOfUseCalendar calendar, TimeOfUse timeOfUse, RateConsumptionFeeType type, RateEventFee rateEventFee) throws InvoiceCalculationException {
        List<RateProfileConsumptionFee> filteredFees = fees.stream()
                .filter(fee -> fee.getCalendar() == null || fee.getCalendar().equals(calendar))
                .filter(fee -> fee.getTimeOfUse() == null || fee.getTimeOfUse().equals(timeOfUse))
                .filter(fee -> fee.getEvent() == null || fee.getEvent().equals(rateEventFee))
                .filter(fee -> fee.getRateType() == null || fee.getRateType().equals(type)).collect(Collectors.toList());
        log.info("fees for more restrictive: "+ fees +"filtered: "+ filteredFees +" calendar: "+calendar+" timeofuse: "+timeOfUse+" type: "+ type+" rateEvent: "+rateEventFee);
        if (CollectionUtils.isEmpty(filteredFees)) {
            return null;
        } else {
            int mostRestrictiveNumber = -1;
            RateProfileConsumptionFee result = null;
            for (RateProfileConsumptionFee filteredFee : filteredFees) {
                if (filteredFee.getRateType() == null) {
                    throw new InvoiceCalculationException("Rate type is wrong, all consumption fees should be either debit or credit");
                }
                int restrictiveIndicator = calculateRestrictiveMagicNumber(filteredFee);
                if(restrictiveIndicator > mostRestrictiveNumber){
                    mostRestrictiveNumber = restrictiveIndicator;
                    result = filteredFee;
                }
            }
            return result;
        }
    }

    private int calculateRestrictiveMagicNumber(RateProfileConsumptionFee filteredFee) {
        int result = 0;
        if (filteredFee.getCalendar() != null) result++;
        if (filteredFee.getTimeOfUse() != null) result++;
        if (filteredFee.getEvent() != null) result+=10;
        return result;
    }


    public RateEventFee findRateEventFee(LocalDateTime localDateTime, BaseServiceAgreement serviceAgreement) {
        Instant date = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        if (serviceAgreement.getEnrollments() != null) {
            for (ProgramServiceAgreementEnrollment enrollment : serviceAgreement.getEnrollments()) {
                if (enrollment.getLocations() != null) {
                    for (LocationSubmissionStatus location : enrollment.getLocations()) {
                        if (location.getEventAppearances() != null) {
                            for (ImpactedCustomer impactedCustomer : location.getEventAppearances()) {
                                Instant start = impactedCustomer.getEvent().getStartDate().toInstant();
                                Instant end = impactedCustomer.getEvent().getEndDate().toInstant();
                                if(start.isBefore(date) && (end.equals(date) || end.isAfter(date))){
                                    switch (impactedCustomer.getEvent().getProgram().getName()) {
                                        case "PDP":
                                            return RateEventFee.PDP;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //TODO apply tier penalty
    private BigDecimal applyTierPenalty(BigDecimal tierUnpaidWatts, int hour, LocalDate date, Invoice invoice, RateProfileConsumptionFee fee, RateTier tier,
                                        BigDecimal tierFromAmountValue) {
        if(tier.getFromAmountValue()!=null){
            InvoiceLine invoiceLine;
            if (tier.getPenaltyPeriod().equals(TierPenaltyPeriod.PER_DAY) && hour == 23) {
                BigDecimal totalConsumptionPerDay = meterService.getTotalConsumption("1721192805", date.format(dayFormatter));
                if (totalConsumptionPerDay.compareTo(tierFromAmountValue) >= 0) {
                    BigDecimal wattsFitInRange = totalConsumptionPerDay.min(tier.getToAmountValue().subtract(tierFromAmountValue));
                    if (wattsFitInRange.longValue() <= 0) {
                        return tierUnpaidWatts;
                    }
//                    invoiceLine = new InvoiceLine(tier, wattsFitInRange.longValue(), fee.getPrice(), invoice);
//                    invoiceService.saveInvoiceLine(invoiceLine);
                }
            }

            if (tier.getPenaltyPeriod().equals(TierPenaltyPeriod.PER_INVOICE)) {
                BigDecimal totalWattsForPeriod = invoiceService.getTotalConsumptionByInvoice(invoice);
                if (totalWattsForPeriod.compareTo(tierFromAmountValue) >= 0) {
                    BigDecimal wattsFitInRange = totalWattsForPeriod.add(tierUnpaidWatts).min(tier.getToAmountValue()).subtract(totalWattsForPeriod);
                    if (wattsFitInRange.longValue() <= 0) {
                        return tierUnpaidWatts;
                    }
                    invoiceLine = invoiceService.getInvoiceLine(tier, invoice);
                    if (invoiceLine == null) {
//                        invoiceLine = new InvoiceLine(tier, fee.getPrice(), invoice);
                    }
                    //invoiceLine.addWatts(wattsFitInRange.longValue());
                    invoiceService.saveInvoiceLine(invoiceLine);

                    tierUnpaidWatts = tierUnpaidWatts.subtract(wattsFitInRange);
                }
            }
        }
        return tierUnpaidWatts;
    }

    @Transactional("mysqlTransactionManager")
    public void submitChargesToUtility(Long invoiceId) {
//        try {
//            TimeUnit.MINUTES.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        invoice.setDueDate(invoice.getDateTo().plusMonths(1));


        invoice.setInvoiceNumber("SMUD-"+invoice.getId());



        try {
            invoiceService.saveInvoice(invoice);
        } catch (IOException e) {
            log.warn(ElasticClientService.INVOICE_MODIFICATIONS_POPULATION_TO_ELASTIC_SEARCH_FAILED,e);
            throw new RuntimeException(e); //To rollback the transaction
        }
    }

    @Transactional("mysqlTransactionManager")
    public void receivePayments(Long invoiceId) {
        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        Payment payment = Payment.builder()
                    .type(PaymentType.BANK_TRANSFER)
                    .date(LocalDateTime.now())
                    .paidBy("Admin")
                    .value(invoice.getTotal())
                    .serviceAgreement(invoice.getServiceAgreement())
                    .build();
        paymentService.savePayment(payment);
        invoice.setPaymentDate(LocalDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        try {
            invoiceService.saveInvoice(invoice);
        } catch (IOException e) {
            log.warn(ElasticClientService.INVOICE_MODIFICATIONS_POPULATION_TO_ELASTIC_SEARCH_FAILED,e);
            throw new RuntimeException(e);  //To rollback the transaction (the payment saving)
        }
    }
}