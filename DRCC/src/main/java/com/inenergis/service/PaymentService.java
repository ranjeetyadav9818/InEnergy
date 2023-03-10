package com.inenergis.service;

import com.inenergis.dao.PaymentDao;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.Payment;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PaymentService {

    @Inject
    private PaymentDao paymentDao;
    @Inject
    private InvoiceService invoiceService;

    @Transactional
    public void save(Payment payment) {
        paymentDao.save(payment);
        payment.getServiceAgreement().getPayments().add(payment);
        checkPaidInvoices(payment.getServiceAgreement());
    }

    public List<Payment> getAll() {
        return paymentDao.getAll();
    }

    public Payment getById(Long id) {
        return paymentDao.getById(id);
    }

    public void checkPaidInvoices(BaseServiceAgreement serviceAgreement) {
        final Long totalPaid = serviceAgreement.getInvoices().stream().filter(i -> i.getPaymentDate() != null).collect(Collectors.summingLong(i -> i.getTotal()));
        final Long creditTotal = serviceAgreement.getPayments().stream().mapToLong(Payment::getValue).sum();
        Long availableCredit = creditTotal - totalPaid;
        if (availableCredit > 0L) {
            Long availableCreditBeforeAssignment = availableCredit;
            final List<Invoice> invoicesToPay = serviceAgreement.getInvoices().stream().filter(i -> i.getPaymentDate() == null)
                    .sorted(Comparator.comparing(Invoice::getDueDate)).collect(Collectors.toList());
            availableCredit = assignCreditToInvoices(availableCredit, invoicesToPay);
            if (availableCreditBeforeAssignment != availableCredit) {
                for (Invoice invoice : invoicesToPay) {
                    final Invoice updatedInvoice = invoiceService.saveOrUpdate(invoice);
                    serviceAgreement.getInvoices().remove(invoice);
                    serviceAgreement.getInvoices().add(updatedInvoice);
                }
            }
        }
    }

    private Long assignCreditToInvoices(Long availableCredit, List<Invoice> invoicesToPay) {
        for (Invoice invoice : invoicesToPay) {
            if (invoice.getTotal() <= availableCredit) {
                availableCredit -= invoice.getTotal();
                invoice.setPaymentDate(LocalDateTime.now());
            } else {
                return availableCredit;
            }
        }
        return availableCredit;
    }
}