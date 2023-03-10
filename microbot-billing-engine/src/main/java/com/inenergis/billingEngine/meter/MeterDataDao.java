package com.inenergis.billingEngine.meter;

import com.inenergis.entity.meterData.IntervalData;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MeterDataDao extends Repository<IntervalData, String> {

    @Transactional("redshiftTransactionManager")
    List<IntervalData> findAllByServicePointId(String servicePointId);

    @Transactional("redshiftTransactionManager")
    List<IntervalData> findByServicePointIdAndTimeGreaterThan(String servicePointId, String time);

    @Transactional("redshiftTransactionManager")
    List<IntervalData> findByServicePointIdAndTimeGreaterThanEqual(String servicePointId, String time);

    @Transactional("redshiftTransactionManager")
    List<IntervalData> findByServicePointIdAndDateGreaterThanEqualAndDateLessThanEqualOrderByTimeAsc(String servicePointId, String dateFrom, String dateTo);
 
    @Transactional("mysqlTransactionManager")
    List<IntervalData> findByServicePointId(String servicePointId);
}
