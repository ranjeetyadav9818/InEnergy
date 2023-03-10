package com.inenergis.billingEngine.service;

import com.inenergis.entity.meterData.PeakDemandMeterData;
import com.inenergis.billingEngine.meter.PeakDemandMeterDataDao;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DemandMeterService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    PeakDemandMeterDataDao peakDemandMeterDataDao;

    @Transactional("redshiftTransactionManager")
    public List<Pair<LocalDateTime,BigDecimal>> getDemandMeterDataBetweenDates(String servicePointId, LocalDate from, LocalDate to) {
        String dateFromFormatted = from.format(dayFormatter);
        String dateToFormatted = to.format(dayFormatter);
        List<Pair<LocalDateTime,BigDecimal>> result = new ArrayList<>();
        for (PeakDemandMeterData peakDemandMeterData : peakDemandMeterDataDao.findByServicePointIdAndDateGreaterThanEqualAndDateLessThanEqualOrderByTimeAsc(servicePointId, dateFromFormatted, dateToFormatted)) {
            LocalDateTime ldt = LocalDateTime.parse(peakDemandMeterData.getTime(), formatter).withMinute(0);
            result.add(ImmutablePair.of(ldt,peakDemandMeterData.getValue()));
        }
        return result;
    }
}