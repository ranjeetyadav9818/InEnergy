package com.inenergis.billingEngine.service.billing;

import com.inenergis.billingEngine.exception.InvoiceCalculationException;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.BaseServicePoint;
import com.inenergis.entity.billing.BillingException;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.genericEnum.RateAncillaryFrequency;
import com.inenergis.entity.genericEnum.RatePercentageAncillaryApplicability;
import com.inenergis.entity.genericEnum.RatePercentageAncillaryType;
import com.inenergis.entity.genericEnum.TierType;
import com.inenergis.entity.genericEnum.TimeOfUseDayType;
import com.inenergis.entity.genericEnum.USStates;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.masterCalendar.TimeOfUseDay;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.program.rateProgram.PercentageFeeHierarchyEntry;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryFee;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryPercentageFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import com.inenergis.billingEngine.service.DemandMeterService;
import com.inenergis.billingEngine.service.PercentageFeeHierarchyService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Component
public class AncillaryCalculationsService {

    private static final Logger log = LoggerFactory.getLogger(AncillaryCalculationsService.class);

    @Autowired
    private CommonBillingService commonBillingService;
    @Autowired
    private DemandMeterService demandMeterService;
    @Autowired
    private PercentageFeeHierarchyService percentageFeeHierarchyService;

    public void calculateAncillaryFees(BaseServicePoint servicePoint, RatePlanProfile activeProfile, Invoice invoice, List<InvoiceLine> invoiceLines, List<BillingException> billingExceptions, Map<LocalDate, RateTier> dateRateTierMap) {
        Map<String, List<RateProfileAncillaryFee>> tierFeeMap = activeProfile.getActiveAncillaryFees().stream().collect(Collectors.groupingBy(RateProfileAncillaryFee::getName));
        for (Map.Entry<String, List<RateProfileAncillaryFee>> rateTierListEntry : tierFeeMap.entrySet()) {
            RateProfileAncillaryFee firstFee = rateTierListEntry.getValue().get(0);
            switch (firstFee.getFrequency()) {
                case KW_DAY:
                case METER_DAY: // It will be called one per SP already
                case PER_DAY:
                    List<Pair<RateProfileAncillaryFee, BigDecimal>> feesAndValues = getTotalValueForAllDays(invoice, rateTierListEntry.getValue(), dateRateTierMap, activeProfile, servicePoint);
                    for (Pair<RateProfileAncillaryFee, BigDecimal> feeAndValue : feesAndValues) {
                        invoiceLines.add(commonBillingService.generateInvoiceLine(feeAndValue.getKey(), rateTierListEntry.getKey(), feeAndValue.getValue(), invoice, servicePoint));
                    }
                    break;
                case KW_INVOICE:
                    List<RateProfileAncillaryFee> ratesWithoutCalendar = rateTierListEntry.getValue().stream().filter(fee -> fee.getCalendar() == null).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(ratesWithoutCalendar)) {
                        Pair<RateProfileAncillaryFee, BigDecimal> pairFeeAndValue = getTotalValueForInvoice(invoiceLines, ratesWithoutCalendar, invoice, servicePoint);
                        invoiceLines.add(commonBillingService.generateInvoiceLine(pairFeeAndValue.getKey(), rateTierListEntry.getKey(), pairFeeAndValue.getValue(), invoice, servicePoint));
                    }
                    Map<TimeOfUseCalendar, List<RateProfileAncillaryFee>> collect = rateTierListEntry.getValue().stream().filter(fee -> fee.getCalendar() != null).collect(Collectors.groupingBy(RateProfileAncillaryFee::getCalendar));
                    for (Map.Entry<TimeOfUseCalendar, List<RateProfileAncillaryFee>> calendarListEntry : collect.entrySet()) {
                        List<RateProfileAncillaryFee> feesWithoutTOU = calendarListEntry.getValue().stream().filter(fee -> fee.getTimeOfUse() == null).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(feesWithoutTOU)){
                            calculateDemandFee(servicePoint, invoice, invoiceLines, rateTierListEntry, calendarListEntry, feesWithoutTOU);
                        }
                        Map<TimeOfUse, List<RateProfileAncillaryFee>> feesWithTOU = calendarListEntry.getValue().stream().filter(fee -> fee.getTimeOfUse() != null).collect(Collectors.groupingBy(RateProfileAncillaryFee::getTimeOfUse));
                        for (Map.Entry<TimeOfUse, List<RateProfileAncillaryFee>> touListEntry : feesWithTOU.entrySet()) {
                            calculateDemandFee(servicePoint, invoice, invoiceLines, rateTierListEntry, calendarListEntry, touListEntry.getValue());
                        }
                    }
                    break;
                case INVOICE:
                    Pair<RateProfileAncillaryFee, BigDecimal> invoiceFeeAndValue = getTotalValueForInvoice(invoiceLines, rateTierListEntry.getValue(), invoice, servicePoint);
                    invoiceLines.add(commonBillingService.generateInvoiceLine(invoiceFeeAndValue.getKey(), rateTierListEntry.getKey(), invoiceFeeAndValue.getValue(), invoice, servicePoint));
                    break;
                case ONE_TIME:
                    if (invoice.getServiceAgreement().getInvoices().stream().noneMatch(i -> i.getInvoiceLines().stream().anyMatch(l->l.getRateProfileAncillaryFee() != null
                                    && l.getRateProfileAncillaryFee().getName().equals(firstFee.getName())
                                    && l.getRateProfileAncillaryFee().getFrequency()==RateAncillaryFrequency.ONE_TIME))) {
                        RateProfileAncillaryFee firstTierFee = rateTierListEntry.getValue().stream().min(Comparator.comparing(IdentifiableEntity::getId)).get();
                        Pair<RateProfileAncillaryFee, BigDecimal> invoiceTotalFeeAndValue = Pair.of(firstTierFee, firstTierFee.getPrice());
                        invoiceLines.add(commonBillingService.generateInvoiceLine(invoiceTotalFeeAndValue.getKey(), rateTierListEntry.getKey(), invoiceTotalFeeAndValue.getValue(), invoice, servicePoint));
                    }
                    break;
            }
        }
        try {
            List<RateProfileAncillaryPercentageFee> activeAncillaryPercentageFees = orderPercentageFees(activeProfile.getActiveAncillaryPercentageFees());
            for (RateProfileAncillaryPercentageFee fee : activeAncillaryPercentageFees) {
                BigDecimal percentage = calculatePercentage(fee, servicePoint);
                BigDecimal calendarMultiplier = calculateCalendarMultiplier(fee, invoice.getDateFrom(), invoice.getDateTo());
                BigDecimal applicableCharges = calculateApplicableCharges(fee, invoiceLines, servicePoint);
                invoiceLines.add(commonBillingService.generateInvoiceLine(fee, fee.getName(), percentage.multiply(calendarMultiplier).multiply(applicableCharges), invoice, servicePoint));
            }
        } catch (InvoiceCalculationException e) {
            billingExceptions.add(BillingException.builder().invoice(invoice).message(e.getMessage()).build());
        }
    }

    private BigDecimal calculateApplicableCharges(RateProfileAncillaryPercentageFee fee, List<InvoiceLine> invoiceLines, BaseServicePoint servicePoint) {
        List<InvoiceLine> applicableLines = invoiceLines.stream().filter(l -> servicePoint == null || servicePoint.equals(l.getServicePoint())).collect(Collectors.toList());
        Stream<InvoiceLine> resultStream = null;
        switch (fee.getApplicability()){
            case ALL:
                resultStream = applicableLines.stream();
                break;
            case SELECTED:
                resultStream = applicableLines.stream().filter(line -> fee.getApplicableFees().contains(getFeeName(line)));
                break;
            case UNSELECTED:
                resultStream = applicableLines.stream().filter(line -> !fee.getApplicableFees().contains(getFeeName(line)));
                break;
        }
        return new BigDecimal(resultStream.mapToLong(l -> l.getTotal()).sum()).divide(new BigDecimal(100).setScale(2));
    }

    private String getFeeName(InvoiceLine line) {
        if (line.getRateProfileAncillaryFee() != null) {
            return line.getRateProfileAncillaryFee().getName();
        }
        if (line.getRateProfileConsumptionFee() != null) {
            return line.getRateProfileConsumptionFee().getName();
        }
        if (line.getRateProfileAncillaryPercentageFee() != null) {
            return line.getRateProfileAncillaryPercentageFee().getName();
        }
        return null;
    }

    private BigDecimal calculateCalendarMultiplier(RateProfileAncillaryPercentageFee fee, LocalDate dateFrom, LocalDate dateTo) {
        if (fee.getCalendar() == null){
            return BigDecimal.ONE;
        } else {
            LocalDate dateFromToCompare = dateFrom;
            LocalDate dateToToCompare = dateTo;
            if(fee.getCalendar().getDateFrom().isAfter(dateFromToCompare)){
                dateFromToCompare = fee.getCalendar().getDateFrom();
            }
            if(fee.getCalendar().getDateTo().isBefore(dateToToCompare)) {
                dateToToCompare = fee.getCalendar().getDateTo();
            }
            long days = Math.max(ChronoUnit.DAYS.between(dateFromToCompare, dateToToCompare) + 1, 0L);
            long total = ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;
            return new BigDecimal(days).divide(new BigDecimal(total), 6, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal calculatePercentage(RateProfileAncillaryPercentageFee fee, BaseServicePoint servicePoint) {
        if (fee.getType().equals(RatePercentageAncillaryType.MANUAL)){
            return fee.getPrice();
        } else {
            for (PercentageFeeHierarchyEntry feeEntry : percentageFeeHierarchyService.getByName(fee.getDynamicFee())) {
                switch (feeEntry.getType()){
                    case CITY:
                        if (feeEntry.getArea().equalsIgnoreCase(servicePoint.getPremise().getServiceCityUpr())){
                            return feeEntry.getPercentage();
                        }
                        break;
                    case COUNTY:
                        if (feeEntry.getArea().equalsIgnoreCase(servicePoint.getPremise().getCounty())){
                            return feeEntry.getPercentage();
                        }
                        break;
                    case STATE:
                        USStates feeState = USStates.getByName(feeEntry.getArea());
                        USStates userState = USStates.getByAbbreviation(servicePoint.getPremise().getServiceState());
                        if (feeState.equals(userState)){
                            return feeEntry.getPercentage();
                        }
                        break;
                    case COUNTRY:
                        return feeEntry.getPercentage();
                }
            }
            return null;
        }
    }

    private List<RateProfileAncillaryPercentageFee> orderPercentageFees(List<RateProfileAncillaryPercentageFee> activeAncillaryPercentageFees) throws InvoiceCalculationException {
        List<RateProfileAncillaryPercentageFee> result = new ArrayList<>();
        result.addAll(activeAncillaryPercentageFees.stream().filter(fee -> fee.getApplicability().equals(RatePercentageAncillaryApplicability.ALL)).collect(Collectors.toList()));
        List<RateProfileAncillaryPercentageFee> pendingToAssign = activeAncillaryPercentageFees.stream().filter(fee -> !fee.getApplicability().equals(RatePercentageAncillaryApplicability.ALL)).collect(Collectors.toList());
        while (result.size() < activeAncillaryPercentageFees.size()) {
            Iterator<RateProfileAncillaryPercentageFee> iterator = pendingToAssign.iterator();
            int currentSize = result.size();
            while (iterator.hasNext()){
                RateProfileAncillaryPercentageFee next = iterator.next();
                if(noOneRefersTo(next,pendingToAssign)){
                    result.add(0,next);
                    iterator.remove();
                }
            }
            if( currentSize == result.size()) {
                throw new InvoiceCalculationException("There is a circular reference in the ancillary percentage fees");
            }
        }
        return result;
    }

    private boolean noOneRefersTo(RateProfileAncillaryPercentageFee next, List<RateProfileAncillaryPercentageFee> pendingToAssign) {
        for (RateProfileAncillaryPercentageFee ancillaryPercentageFee : pendingToAssign) {
            if(!ancillaryPercentageFee.equals(next)){
                if (ancillaryPercentageFee.getApplicability().equals(RatePercentageAncillaryApplicability.SELECTED)){
                    if (ancillaryPercentageFee.getApplicableFees().contains(next.getName())) {
                        return false;
                    }
                } else {
                    if (!ancillaryPercentageFee.getApplicableFees().contains(next.getName())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void calculateDemandFee(BaseServicePoint servicePoint, Invoice invoice, List<InvoiceLine> invoiceLines, Map.Entry<String, List<RateProfileAncillaryFee>> rateTierListEntry,
                                   Map.Entry<TimeOfUseCalendar, List<RateProfileAncillaryFee>> calendarListEntry, List<RateProfileAncillaryFee> feesWithoutTOU) {

        Pair<RateProfileAncillaryFee, BigDecimal> pairFeeAndValue = getTotalValueForInvoice(invoiceLines, feesWithoutTOU, invoice, servicePoint);
        pairFeeAndValue = Pair.of(pairFeeAndValue.getLeft(), pairFeeAndValue.getValue().divide(daysInTheInvoice(invoice), 8, BigDecimal.ROUND_HALF_UP).multiply(calendarDaysInTheInvoice(invoice, calendarListEntry.getKey())));
        invoiceLines.add(commonBillingService.generateInvoiceLine(pairFeeAndValue.getKey(), rateTierListEntry.getKey(), pairFeeAndValue.getValue(), invoice, servicePoint));
    }

    private BigDecimal calendarDaysInTheInvoice(Invoice invoice, TimeOfUseCalendar calendar) {
        LocalDate from = invoice.getDateFrom();
        if (calendar.getDateFrom().isAfter(from)) {
            from = calendar.getDateFrom();
        }
        LocalDate to = invoice.getDateTo();
        if (calendar.getDateTo().isBefore(to)) {
            to = calendar.getDateTo();
        }
        return new BigDecimal(ChronoUnit.DAYS.between(from, to) + 1);
    }

    private BigDecimal daysInTheInvoice(Invoice invoice) {
        return new BigDecimal(ChronoUnit.DAYS.between(invoice.getDateFrom(), invoice.getDateTo()) + 1);
    }

    public BigDecimal findMultiplierForKWFee(BaseServicePoint servicePoint, LocalDate currentDate, RateProfileAncillaryFee fee, List<Pair<LocalDateTime, BigDecimal>> demandData) {
        if (fee.getFrequency().equals(RateAncillaryFrequency.KW_DAY)) {
            return getTotalDemand(fee, servicePoint, demandData.stream().filter(d -> dateTimeInDate(d.getKey(),currentDate)).collect(Collectors.toList()));
        } else {
            return BigDecimal.ONE;
        }
    }

    private boolean dateTimeInDate(LocalDateTime dateTime, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return (start.isEqual(dateTime) || start.isBefore(dateTime)) && end.isAfter(dateTime);
    }

    private BigDecimal getTotalDemand(RateProfileAncillaryFee fee, BaseServicePoint servicePoint, List<Pair<LocalDateTime, BigDecimal>> demandData) {
        if (CollectionUtils.isEmpty(demandData)) {
            return servicePoint.getMaxDemand()==null?BigDecimal.ZERO:servicePoint.getMaxDemand().divide(new BigDecimal(1000), 3, RoundingMode.HALF_UP);
        } else {
            Iterator<Pair<LocalDateTime, BigDecimal>> iterator = demandData.iterator();
            List<BigDecimal> result = new ArrayList<>();
            while (iterator.hasNext()) {
                Pair<LocalDateTime, BigDecimal> timeAndDemandValue = iterator.next();
                if (!valueIsNotInCalendarOrTOU(timeAndDemandValue, fee.getCalendar(), fee.getTimeOfUse())) {
                    result.add(timeAndDemandValue.getValue());
                }
            }
            if (result.isEmpty()) {
                return BigDecimal.ZERO;
            } else {
                return result.stream().max(Comparator.naturalOrder()).get();
            }
        }
    }

    public boolean valueIsNotInCalendarOrTOU(Pair<LocalDateTime, BigDecimal> timeAndDemandValue, TimeOfUseCalendar calendar, TimeOfUse timeOfUse) {
        final LocalDateTime time = timeAndDemandValue.getKey();
        if (calendar != null) {
            if (time.isBefore(calendar.getDateFrom().atStartOfDay()) || time.isAfter(calendar.getDateTo().plusDays(1).atStartOfDay().minusSeconds(1))) {
                return true;
            }
        }
        if (timeOfUse != null) {
            TimeOfUseDayType type = time.getDayOfWeek() == DayOfWeek.SATURDAY || time.getDayOfWeek() == DayOfWeek.SUNDAY ? TimeOfUseDayType.WEEK_ENDS : TimeOfUseDayType.WEEK_DAYS;
            //TODO We are not considering holidays
            final Optional<TimeOfUseDay> first = timeOfUse.getTimeOfUseDays().stream()
                    .filter(tou -> tou.getDay() == type)
                    .filter(t -> t.getTimeOfUse().getTimeOfUseHours().stream().anyMatch(h -> time.plusHours(1).getHour() == h.getHour())).findFirst();
            return ! first.isPresent();
        }
        return false;
    }

    private List<Pair<RateProfileAncillaryFee, BigDecimal>> getTotalValueForAllDays(Invoice invoice, List<RateProfileAncillaryFee> ancillaryFees, Map<LocalDate, RateTier> dateRateTierMap, RatePlanProfile planProfile, BaseServicePoint servicePoint) {
        Map<RateProfileAncillaryFee, BigDecimal> runningCalculations = new HashMap<>();

        List<Pair<LocalDateTime, BigDecimal>> demandData = demandMeterService.getDemandMeterDataBetweenDates(servicePoint.getServicePointId(), invoice.getDateFrom(), invoice.getDateTo());
        LocalDate currentDate = invoice.getDateFrom();
        while (!invoice.getDateTo().isBefore(currentDate)) {
            RateTier tier = getHighestTierForDate(currentDate, dateRateTierMap, planProfile);
            TimeOfUseCalendar calendar = commonBillingService.getCalendarByDate(null, currentDate);
            RateProfileAncillaryFee feeByTier = getFeeByTier(ancillaryFees, tier, calendar);
            if (feeByTier != null) {
                if (feeByTier.getFrequency().equals(RateAncillaryFrequency.KW_DAY)) {
                    List<RateProfileAncillaryFee> fees = getFeesByTier(ancillaryFees, tier, calendar);

                    for (RateProfileAncillaryFee fee : fees) {
                        if (!runningCalculations.containsKey(fee)) {
                            runningCalculations.put(fee, BigDecimal.ZERO);
                        }
                        BigDecimal multiplier = findMultiplierForKWFee( servicePoint, currentDate, fee, demandData);
                        runningCalculations.put(fee, runningCalculations.get(fee).add(fee.getPrice().multiply(multiplier)));
                    }
                } else {
                    if (!runningCalculations.containsKey(feeByTier)) {
                        runningCalculations.put(feeByTier, BigDecimal.ZERO);
                    }
                    runningCalculations.put(feeByTier, runningCalculations.get(feeByTier).add(feeByTier.getPrice()));
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        List<Pair<RateProfileAncillaryFee, BigDecimal>> result = new ArrayList<>();
        for (Map.Entry<RateProfileAncillaryFee, BigDecimal> entry : runningCalculations.entrySet()) {
            result.add(Pair.of(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private RateTier getHighestTierForDate(LocalDate currentDate, Map<LocalDate, RateTier> dateRateTierMap, RatePlanProfile planProfile) {
        if (planProfile.getTierType().equals(TierType.DEMAND)) {
            return commonBillingService.getHighestTier(dateRateTierMap.values());
        } else {
            return dateRateTierMap.get(currentDate);
        }
    }

    private Pair<RateProfileAncillaryFee, BigDecimal> getTotalValueForInvoice(List<InvoiceLine> invoiceLines, List<RateProfileAncillaryFee> fees, Invoice invoice, BaseServicePoint servicePoint) {
        RateTier highestTier = commonBillingService.getHighestTier(invoiceLines.stream().map(InvoiceLine::getTier).collect(Collectors.toList()));
        RateProfileAncillaryFee fee = getFeeByTier(fees, highestTier, null);
        return Pair.of(fee, fee.getPrice().multiply(getMultiplier(fee, servicePoint, demandMeterService.getDemandMeterDataBetweenDates(servicePoint.getServicePointId(), invoice.getDateFrom(), invoice.getDateTo()))));
    }

    private BigDecimal getMultiplier(RateProfileAncillaryFee fee, BaseServicePoint servicePoint, List<Pair<LocalDateTime, BigDecimal>> demandData) {
        if (fee.getFrequency().equals(RateAncillaryFrequency.KW_INVOICE)) {
            return getTotalDemand(fee, servicePoint, demandData);
        } else {
            return new BigDecimal(1);
        }
    }

    private RateProfileAncillaryFee getFeeByTier(List<RateProfileAncillaryFee> fees, RateTier highestTier, TimeOfUseCalendar calendar) {
        if (highestTier == null) {
            return fees.get(0);
        }
        Stream<RateProfileAncillaryFee> rateProfileAncillaryFeeStream = fees.stream().filter(f -> f.getRateTier() != null && f.getRateTier().equals(highestTier));
        if (calendar != null) {
            rateProfileAncillaryFeeStream = rateProfileAncillaryFeeStream.filter(f -> f.getCalendar() == null || f.getCalendar().equals(calendar));
        }
        Optional<RateProfileAncillaryFee> first = rateProfileAncillaryFeeStream.findFirst();
        return first.orElse(null);
    }

    private List<RateProfileAncillaryFee> getFeesByTier(List<RateProfileAncillaryFee> fees, RateTier highestTier, TimeOfUseCalendar calendar) {
        if (highestTier == null) {
            return fees;
        }
        Stream<RateProfileAncillaryFee> rateProfileAncillaryFeeStream = fees.stream().filter(f -> f.getRateTier() != null && f.getRateTier().equals(highestTier));
        if (calendar != null) {
            rateProfileAncillaryFeeStream = rateProfileAncillaryFeeStream.filter(f -> f.getCalendar() == null || f.getCalendar().equals(calendar));
        }
        return rateProfileAncillaryFeeStream.collect(Collectors.toList());

    }
}