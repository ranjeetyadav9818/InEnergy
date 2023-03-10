package com.inenergis.dao;

import com.inenergis.entity.BillingCycleSchedule;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class BillingCycleDao extends GenericDao<BillingCycleSchedule> {
    public BillingCycleDao() {
        setClazz(BillingCycleSchedule.class);
    }

}
