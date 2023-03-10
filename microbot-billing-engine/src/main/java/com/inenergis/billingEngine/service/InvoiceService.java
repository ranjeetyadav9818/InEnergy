package com.inenergis.billingEngine.service;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.program.rateProgram.RateTier;
import com.inenergis.model.ElasticInvoice;
import com.inenergis.model.ElasticInvoiceConverter;
import com.inenergis.billingEngine.sa.InvoiceDao;
import com.inenergis.billingEngine.sa.InvoiceLineDao;
import com.inenergis.util.ConstantsProviderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InvoiceService {

    @Autowired
    private InvoiceLineDao invoiceLineDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private ElasticClientService elasticClienService;


    public InvoiceLine saveInvoiceLine(InvoiceLine invoiceLine) {
        return invoiceLineDao.save(invoiceLine);
    }

    public BigDecimal getTotalConsumptionByInvoice(Invoice invoice) {
        if (invoice == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = invoiceLineDao.getTotalConsumptionByInvoiceId(invoice.getId());
        if (total == null) {
            total = BigDecimal.ZERO;
        }

        return total;
    }

    @Transactional("mysqlTransactionManager")
    public Invoice getInvoiceByDate(LocalDate dateFrom, LocalDate dateTo, BaseServiceAgreement serviceAgreement) {
        Invoice invoice = invoiceDao.getByDateFromLessThanEqualAndDateToGreaterThanAndServiceAgreement(dateFrom, dateFrom, serviceAgreement);
        if (invoice == null) {
            invoice = new Invoice();
            invoice.setDateFrom(dateFrom);
            invoice.setDateTo(dateTo);
            invoice.setDate(LocalDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
            invoice.setServiceAgreement(serviceAgreement);
        } else {
            invoice.getInvoiceLines().size();
            invoice.getExceptions().size();
        }

        return invoice;
    }

    public InvoiceLine getInvoiceLine(RateTier rateTier, Invoice invoice) {
        return invoiceLineDao.getInvoiceLineByTierAndInvoice(rateTier, invoice);
    }

    @Transactional("mysqlTransactionManager")
    public Invoice saveInvoice(Invoice invoice) throws IOException {
        final Invoice invoiceSaved = invoiceDao.save(invoice);
        elasticClienService.indexDocument(invoice.getId().toString(), ElasticInvoiceConverter.convert(invoiceSaved), ElasticClientService.ENERGY_ARRAY_INDEX,ElasticInvoice.INVOICE);
        return invoiceSaved;
    }

    @Transactional("mysqlTransactionManager")
    public Invoice getInvoiceById(Long id) {
        Invoice invoice = invoiceDao.getById(id);
        invoice.getInvoiceLines().size();
        invoice.getExceptions().size();
        return invoice;
    }

    public List<Invoice> getInvoicesByStatus(Invoice.InvoiceStatus status) {
        return invoiceDao.getByStatus(status);
    }
}
