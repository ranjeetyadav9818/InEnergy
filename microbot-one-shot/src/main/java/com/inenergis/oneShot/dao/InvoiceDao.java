package com.inenergis.oneShot.dao;

import com.inenergis.entity.billing.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by egamas on 05/09/2017.
 */
public interface InvoiceDao extends Repository<Invoice, String> {

    Page<Invoice> findAll(Pageable pageable);
}