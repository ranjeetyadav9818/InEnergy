package com.inenergis.billingEngine.service;

import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.meterData.IntervalData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ConsumptionService {

    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Cacheable("highest-consumption-per-calendar")
    public BigDecimal calculateHighestConsumption(List<IntervalData> meterDataUsage, TimeOfUseCalendar calendar) {
        BigDecimal result = BigDecimal.ZERO;
        for (IntervalData meterData : meterDataUsage) {
            LocalDate meterDate = LocalDate.parse(meterData.getDate(), dayFormatter);
            if(!(meterDate.isBefore(calendar.getDateFrom()) || meterDate.isAfter(calendar.getDateTo()))){
                result = result.max(meterData.getValue());
            }
        }
        return result;
    }
}