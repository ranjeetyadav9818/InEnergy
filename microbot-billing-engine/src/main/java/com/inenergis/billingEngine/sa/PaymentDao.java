package com.inenergis.billingEngine.sa;

import com.inenergis.entity.Event;
import com.inenergis.entity.billing.Payment;
import org.springframework.data.repository.Repository;

import java.util.Date;

public interface PaymentDao extends Repository<Payment, Long> {

    void save(Payment payment);
}
