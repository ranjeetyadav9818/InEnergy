package com.inenergis.dao;

import com.inenergis.entity.billing.BillingException;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class BillingExceptionDao extends GenericDao<BillingException> {
    public BillingExceptionDao() {
        setClazz(BillingException.class);
    }

}
