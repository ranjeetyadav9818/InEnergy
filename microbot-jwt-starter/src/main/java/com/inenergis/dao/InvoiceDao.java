package com.inenergis.dao;

import com.inenergis.entity.billing.Invoice;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egamas on 04/10/2017.
 */
public interface InvoiceDao extends Repository<Invoice,Long> {

    @Transactional("mysqlTransactionManager")
    Invoice getFirstByServiceAgreementServiceAgreementIdAndStatusOrderByDateToDesc(String serviceAgreementId, Invoice.InvoiceStatus status);
}
