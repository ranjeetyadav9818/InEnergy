package com.inenergis.billingEngine.sa;

import com.inenergis.entity.BaseServicePoint;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface ServicePointDao extends Repository<BaseServicePoint, String> {
    @Transactional("mysqlTransactionManager")
    BaseServicePoint findByServicePointId(String servicePointId);
}
