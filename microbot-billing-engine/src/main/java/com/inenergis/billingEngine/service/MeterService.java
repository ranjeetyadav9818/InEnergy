package com.inenergis.billingEngine.service;

import com.inenergis.entity.meterData.IntervalData;
import com.inenergis.entity.meterData.LatestMeterData;
import com.inenergis.entity.meterData.PeakDemandMeterData;
import com.inenergis.billingEngine.meter.LatestMeterDataDao;
import com.inenergis.billingEngine.meter.MeterDataDao;
import com.inenergis.billingEngine.meter.PeakDemandMeterDataDao;
import com.inenergis.billingEngine.sa.MeterDataUsage;
import com.inenergis.billingEngine.sa.MeterDataUsageDao;
import com.inenergis.util.ConstantsProviderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MeterService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    private MeterDataDao meterDataDao;

    @Autowired
    private PeakDemandMeterDataDao peakDemandMeterDataDao;

    @Autowired
    private LatestMeterDataDao latestMeterDataDao;

    @Autowired
    private MeterDataUsageDao meterDataUsageDao;

    @Transactional("redshiftTransactionManager")
    public Map<Long, BigDecimal> getHourlyMeterData(Long servicePointId, String time) {
        List<IntervalData> meterData = meterDataDao.findByServicePointIdAndTimeGreaterThanEqual(Long.toString(servicePointId), time);


        Map<Long, BigDecimal> hourlyData = new HashMap<>();
        for (IntervalData md : meterData) {
            LocalDateTime ldt = LocalDateTime.parse(md.getTime(), formatter).withMinute(0);
            Long key = Long.valueOf(ldt.format(formatter));

            BigDecimal currentValue = hourlyData.getOrDefault(key, BigDecimal.ZERO);
            hourlyData.put(key, currentValue.add(md.getValue()));
        }

        return hourlyData;
    }

    @Transactional("redshiftTransactionManager")
    public Map<Long, BigDecimal> getDailyMeterData(String servicePointId, String time) {
        List<IntervalData> meterData = meterDataDao.findByServicePointIdAndTimeGreaterThan(servicePointId, time);

        Map<Long, BigDecimal> dailyData = new HashMap<>();
        for (IntervalData md : meterData) {
            LocalDateTime ldt = LocalDateTime.parse(md.getTime(), formatter).withHour(0).withMinute(0);
            Long key = Long.valueOf(ldt.format(formatter));

            BigDecimal currentValue = dailyData.getOrDefault(key, BigDecimal.ZERO);
            dailyData.put(key, currentValue.add(md.getValue()));
        }

        return dailyData;
    }

    @Transactional("redshiftTransactionManager")
    public List<IntervalData> findAllByServicePointId(String servicePointId) {
        return meterDataDao.findAllByServicePointId(servicePointId);
    }

    @Transactional("mysqlTransactionManager")
    public List<IntervalData> getMeterDataUsage(String servicePointId, LocalDate dateFrom, LocalDate dateTo) {
        String dateFromFormatted = dateFrom.format(dayFormatter);
        String dateToFormatted = dateTo.format(dayFormatter);
        return meterDataDao.findByServicePointIdAndDateGreaterThanEqualAndDateLessThanEqualOrderByTimeAsc(servicePointId, dateFromFormatted, dateToFormatted);
    }

    public List<LatestMeterData> getLatestMeterData() {
        return latestMeterDataDao.getLatestMeterData();
    }

    @Transactional("redshiftTransactionManager")
    public BigDecimal getTotalConsumption(String servicePointId, String date) {
        BigDecimal total = latestMeterDataDao.getTotalConsumption(servicePointId, date);
        if (total == null) {
            return BigDecimal.ZERO;
        } else {
            return total.multiply(ConstantsProviderModel.ONE_THOUSAND_BIG_DECIMAL);
        }
    }

    @Transactional("mysqlTransactionManager")
    public MeterDataUsage getLatestMeterDataUsage(String saId) {
        return meterDataUsageDao.findFirstByServicePointIdOrderByDateDesc(saId);
    }

    @Transactional("mysqlTransactionManager")
    public void saveUsage(MeterDataUsage meterDataUsage) {
        meterDataUsageDao.save(meterDataUsage);
    }


    public List<PeakDemandMeterData> findAllPeakByServicePointId(String servicePointId) {
        return peakDemandMeterDataDao.findAllByServicePointId(servicePointId);
    }
}