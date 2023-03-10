package com.inenergis.billingEngine.sa;

import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.program.rateProgram.RateTier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;

public interface InvoiceLineDao extends Repository<InvoiceLine, String> {

    InvoiceLine save(InvoiceLine invoiceLine);

    @Query(value = "SELECT SUM(watts) FROM INVOICE_LINE WHERE INVOICE_ID = ?1 AND `TYPE` = 'TIER'", nativeQuery = true)
    BigDecimal getTotalConsumptionByInvoiceId(Long invoiceId);

    InvoiceLine getInvoiceLineByTierAndInvoice(RateTier rateTier, Invoice invoice);
}