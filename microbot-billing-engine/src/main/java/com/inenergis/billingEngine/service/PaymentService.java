package com.inenergis.billingEngine.service;

import com.inenergis.entity.billing.Payment;
import com.inenergis.billingEngine.sa.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    public void savePayment(Payment payment) {
        paymentDao.save(payment);
    }
}