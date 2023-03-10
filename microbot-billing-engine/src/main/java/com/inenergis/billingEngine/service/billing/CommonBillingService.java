package com.inenergis.billingEngine.service.billing;

import com.inenergis.billingEngine.service.TimeOfUseCalendarService;
import com.inenergis.entity.BaseServicePoint;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryFee;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryPercentageFee;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import com.inenergis.entity.program.rateProgram.RateProfileFee;
import com.inenergis.entity.program.rateProgram.RateTier;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class CommonBillingService {

    private static final Logger log = LoggerFactory.getLogger(CommonBillingService.class);

    @Autowired
    private TimeOfUseCalendarService timeOfUseCalendarService;

    public RateTier getHighestTier(Collection<RateTier> tiers) {
        RateTier result =null;
        for (RateTier tier : tiers) {
            if(result == null){
                result = tier;
            } else if(tier != null && tier.getId() > result.getId()){ // TODO create the concept of order for tiers
                result = tier;
            }
        }
        return result;
    }


    public TimeOfUseCalendar getCalendarByDate(TimeOfUseCalendar calendar, LocalDate meterDate) {
        List<TimeOfUseCalendar> calendars = timeOfUseCalendarService.findByDate(meterDate);
        if (CollectionUtils.isNotEmpty(calendars)) {
            return calendars.get(0);
        }
        return calendar;
    }


    public InvoiceLine generateInvoiceLine(RateProfileFee fee, String concept, BigDecimal value, Invoice invoice, BaseServicePoint servicePoint) {
        InvoiceLine invoiceLine = new InvoiceLine();
        BigDecimal total;
        if(fee instanceof RateProfileConsumptionFee){

            // Gas Changes Innobit
            if(fee.getRatePlanProfile().getRatePlan().getCommodityType().toString().equals("Gas")){

                invoiceLine.setTherms(value);

            }else{
                invoiceLine.setKwh(value);

            }

            total = fee.getPrice().multiply(value);
        }else{
            total = value;
        }
        setCommonInfoInInvoiceLine(concept, invoice, servicePoint, invoiceLine, total);
        invoiceLine.setTier(fee.getRateTier());
        if(fee.getRatePlanProfile().getRatePlan().getCommodityType().toString().equals("Gas")){


            invoiceLine.setPricePerTherms(fee.getPrice());

        }else{

            invoiceLine.setPricePerWatt(fee.getPrice());

        }

        if(fee instanceof RateProfileConsumptionFee){
            invoiceLine.setRateProfileConsumptionFee((RateProfileConsumptionFee) fee);
        } else {
            invoiceLine.setRateProfileAncillaryFee((RateProfileAncillaryFee) fee);
        }
        return invoiceLine;
    }

    public InvoiceLine generateInvoiceLine(RateProfileAncillaryPercentageFee fee, String concept, BigDecimal value, Invoice invoice, BaseServicePoint servicePoint) {
        InvoiceLine invoiceLine = new InvoiceLine();
        setCommonInfoInInvoiceLine(concept, invoice, servicePoint, invoiceLine, value);
        invoiceLine.setRateProfileAncillaryPercentageFee(fee);
        return invoiceLine;
    }

    public void setCommonInfoInInvoiceLine(String concept, Invoice invoice, BaseServicePoint servicePoint, InvoiceLine invoiceLine, BigDecimal total) {
        invoiceLine.setTotal(total.multiply(new BigDecimal(100L)).divide(BigDecimal.ONE, 0, RoundingMode.HALF_UP).longValue());
        invoiceLine.setInvoice(invoice);
        invoiceLine.setConcept(concept);
        invoiceLine.setType(InvoiceLine.ChargeType.RATE);
        invoiceLine.setServicePoint(servicePoint);
    }

    public String generateInvoiceLineConcept(RateProfileConsumptionFee fee) {
        StringBuilder sb = new StringBuilder(fee.getName());
        if(fee.getTimeOfUse() != null){
            sb.append(" - ").append(fee.getTimeOfUse().getTou().getName());
        }
        if (fee.getEvent() != null) {
            sb.append(" - ").append(fee.getEvent().getName());
        }
        return sb.toString();
    }
}