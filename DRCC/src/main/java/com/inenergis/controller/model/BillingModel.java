package com.inenergis.controller.model;

import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.billing.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.inenergis.entity.billing.Invoice.InvoiceStatus.WITH_ERRORS;

@Getter
@Setter
@NoArgsConstructor
public class BillingModel {
    private Invoice invoice;
    private Payment payment;
    private List<InvoiceLine> invoiceLines;

    private String uuid;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalDate duePaidDate;
    private String reference;
    private Long totalCharges;
    private Long totalAncillary;
    private Long totalVat;
    private Long debit;
    private Long credit;
    private LocalDateTime date;
    private boolean hasErrors;

    public BillingModel(Invoice invoice) {
        this.invoice = invoice;
        uuid = invoice.getUuid();
        dateFrom = invoice.getDateFrom();
        dateTo = invoice.getDateTo();
        duePaidDate = invoice.getDueDate();
        reference = invoice.getInvoiceNumber();
        debit = invoice.getTotal();
        date = invoice.getDate();
        hasErrors = invoice.getStatus().equals(WITH_ERRORS);
//        totalCharges = calculateTotalCharges();
//        totalAncillary = calculateTotalAncillary();
//        totalVat = calculateTotalVat();
    }

    public BillingModel(Payment payment) {
        this.payment = payment;
        uuid = payment.getUuid();
        duePaidDate = payment.getDate().toLocalDate();
        reference = payment.getReference();
        credit = payment.getValue();
        date = payment.getDate();
    }

//    public Long calculateTotalCharges() {
//        return invoiceLines.stream().mapToLong(InvoiceLine::getTotal).sum();
//    }
//
//    public Long calculateTotalAncillary() {
//        return invoiceLines.stream().mapToLong(InvoiceLine::getTotal).sum();
//    }
//
//    public Long calculateTotalVat() {
//        return invoiceLines.stream().mapToLong(InvoiceLine::getTotal).sum();
//    }


}
