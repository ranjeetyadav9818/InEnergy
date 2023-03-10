package com.inenergis.dao;

import com.inenergis.entity.billing.Payment;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egamas on 04/10/2017.
 */
public interface PaymentDao extends Repository<Payment,Long> {

    @Transactional("mysqlTransactionManager")
    Payment getFirstByServiceAgreementServiceAgreementIdOrderByDateDesc(String serviceAgreementId);
}
