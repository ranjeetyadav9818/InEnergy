package com.inenergis.billingEngine.sa;

import org.springframework.data.repository.Repository;

public interface MeterDataUsageDao extends Repository<MeterDataUsage, String> {

    MeterDataUsage findFirstByServicePointIdOrderByDateDesc(String servicePointId);

    MeterDataUsage save(MeterDataUsage meterDataUsage);

}