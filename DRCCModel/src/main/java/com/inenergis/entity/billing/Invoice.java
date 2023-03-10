package com.inenergis.entity.billing;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "INVOICE")
public class Invoice extends IdentifiableEntity {

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private InvoiceStatus status;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_FROM")
    private LocalDate dateFrom;

    @Column(name = "DATE_TO")
    private LocalDate dateTo;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "PAID_DATE")
    private LocalDateTime paymentDate;

    @Column(name = "TOTAL_CONSUMPTION")
    private BigDecimal totalConsumption;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLine> invoiceLines;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingException> exceptions;

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    private BaseServiceAgreement serviceAgreement;

    @Transient
    private Long overdueDays;

    public Long getTotal() {
        return invoiceLines.stream().mapToLong(InvoiceLine::getTotal).sum();
    }

    public Invoice() {
        invoiceLines = new ArrayList<>();
    }

    @PostLoad
    public void init() {
        if(dueDate!=null){
            overdueDays = Math.max(0, ChronoUnit.DAYS.between(dueDate, LocalDate.now()));
        }
    }

    public String getInvoiceName() {
        if (invoiceNumber == null) {
            return "Temp. " + getId();
        } else {
            return invoiceNumber;
        }
    }

    public boolean shouldBeSaved() {
        return (getInvoiceLines() != null && getInvoiceLines().size()>0) || (getExceptions() != null && getExceptions().size()>0);
    }

    public enum InvoiceStatus {
        FINAL("Final"),
        WITH_ERRORS("With errors"),
        RETRYING("Marked for retrying"),
        GENERATING("Generating (Work in progress)");

        @Getter
        private String label;

        InvoiceStatus(String name) {
            this.label = name;
        }
    }
}