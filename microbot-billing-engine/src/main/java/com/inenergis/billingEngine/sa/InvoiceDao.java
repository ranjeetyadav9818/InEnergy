package com.inenergis.billingEngine.sa;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceDao extends Repository<Invoice, String> {

    Invoice save(Invoice invoice);

    @Query(value = "SELECT SUM(watts) FROM INVOICE_LINE WHERE INVOICE_ID = ?1 AND `TYPE` = 'TIER'", nativeQuery = true)
    BigDecimal getTotalConsumptionByInvoiceId(Long invoiceId);

    Invoice getByDateFromLessThanEqualAndDateToGreaterThanAndServiceAgreement(LocalDate dateFrom, LocalDate dateTo, BaseServiceAgreement serviceAgreementId);

    Invoice getById(Long id);

    List<Invoice> getByStatus(Invoice.InvoiceStatus status);
}