package com.inenergis.billingEngine.service;

import com.inenergis.entity.BillingCycleSchedule;
import com.inenergis.billingEngine.sa.BillingCycleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class BillingCycleService {

    @Autowired
    private BillingCycleDao billingCycleDao;

    @Transactional("mysqlTransactionManager")
    public BillingCycleSchedule findByDate(LocalDate date) {
        return billingCycleDao.findByDate(date);
    }

    @Transactional("mysqlTransactionManager")
    public void update(BillingCycleSchedule cycle) {
        billingCycleDao.save(cycle);
    }
}