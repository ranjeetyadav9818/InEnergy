package com.inenergis.billingEngine.meter;

import com.inenergis.entity.meterData.PeakDemandMeterData;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PeakDemandMeterDataDao extends Repository<PeakDemandMeterData, String> {

    @Transactional("redshiftTransactionManager")
    List<PeakDemandMeterData> findAllByServicePointId(String servicePointId);

    @Transactional("redshiftTransactionManager")
    List<PeakDemandMeterData> findByServicePointIdAndTimeGreaterThan(String servicePointId, String time);

    @Transactional("redshiftTransactionManager")
    List<PeakDemandMeterData> findByServicePointIdAndTimeGreaterThanEqual(String servicePointId, String time);

    @Transactional("redshiftTransactionManager")
    List<PeakDemandMeterData> findByServicePointIdAndDateGreaterThanEqualAndDateLessThanEqualOrderByTimeAsc(String servicePointId, String dateFrom, String dateTo);
}
