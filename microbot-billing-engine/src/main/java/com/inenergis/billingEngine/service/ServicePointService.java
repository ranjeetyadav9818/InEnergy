package com.inenergis.billingEngine.service;

import com.inenergis.billingEngine.sa.ServicePoint;
import com.inenergis.billingEngine.sa.ServicePointDao;
import com.inenergis.entity.BaseServicePoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ServicePointService {

    @Autowired
    private ServicePointDao servicePointDao;

    @Transactional("mysqlTransactionManager")
    public BaseServicePoint findByServicePointId(String id) {
        return servicePointDao.findByServicePointId(id);
    }
}
