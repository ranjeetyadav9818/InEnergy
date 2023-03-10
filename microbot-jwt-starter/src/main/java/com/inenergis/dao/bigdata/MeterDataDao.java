package com.inenergis.dao.bigdata;

import com.inenergis.model.meter.MeterData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MeterDataDao extends Repository<MeterData, String> {

    @Transactional("redshiftTransactionManager")
    List<MeterData> findAllByServicePointIdIn(List<String> servicePointIds);

    @Transactional("redshiftTransactionManager")
    @Query(value = "select new com.inenergis.dao.bigdata.ConsumptionByMonth(sum(value), substring(date,1,6)) from MeterData where service_point_id in ?1 group by 2 order by 2")
    List<Object> getConsumptionsByMonth(List<String> servicePointIds);
}
