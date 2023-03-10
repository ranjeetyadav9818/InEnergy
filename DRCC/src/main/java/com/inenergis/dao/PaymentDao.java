package com.inenergis.dao;

import com.inenergis.entity.billing.Payment;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class PaymentDao extends GenericDao<Payment> {
    public PaymentDao() {
        setClazz(Payment.class);
    }
}