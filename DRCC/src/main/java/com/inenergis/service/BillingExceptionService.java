package com.inenergis.service;

import com.inenergis.dao.BillingExceptionDao;
import com.inenergis.entity.billing.BillingException;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BillingExceptionService {

    @Inject
    private BillingExceptionDao billingExceptionDao;

    public void saveOrUpdate(BillingException exception) {
        billingExceptionDao.saveOrUpdate(exception);
    }

}