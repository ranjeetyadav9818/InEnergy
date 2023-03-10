package com.inenergis.billingEngine.meter;

import com.inenergis.entity.meterData.LatestMeterData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface LatestMeterDataDao extends Repository<LatestMeterData, String> {

    @Query(value = "SELECT service_point_id, MAX(time) AS time FROM COMMODITY_INTERVAL_DATA GROUP BY service_point_id ORDER BY service_point_id", nativeQuery = true)
    List<LatestMeterData> getLatestMeterData();

    @Transactional("redshiftTransactionManager")
    @Query(value = "SELECT SUM(usage_value) FROM COMMODITY_INTERVAL_DATA WHERE service_point_id = ?1 AND date = ?2", nativeQuery = true)
    BigDecimal getTotalConsumption(String servicePointId, String date);
}