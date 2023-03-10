package com.inenergis.model.adapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.entity.billing.Invoice.InvoiceStatus.FINAL;

/**
 * Created by egamas on 25/09/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAdapter {
    private Long id;
    private String invoiceNumber;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;
    private BigDecimal totalConsumption;
    private Long total;

    public static List<InvoiceAdapter> buildInvoices(ServiceAgreement sa) {
        List<Invoice> invoices = sa.getInvoices();
        invoices.sort(Comparator.comparing(Invoice::getDate).reversed());
        if (CollectionUtils.isNotEmpty(invoices)) {
            return invoices.stream().filter(invoice -> invoice.getStatus().equals(FINAL))
                    .map(inv -> build(inv))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    public static InvoiceAdapter build(Invoice inv){
        return InvoiceAdapter.builder()
                .id(inv.getId())
                .invoiceNumber(inv.getInvoiceNumber())
                .dueDate(inv.getDueDate())
                .description(inv.getDescription())
                .dateFrom(inv.getDateFrom())
                .dateTo(inv.getDateTo())
                .date(inv.getDate())
                .paymentDate(inv.getPaymentDate())
                .totalConsumption(inv.getTotalConsumption())
                .total(inv.getTotal())
                .build();
    }
}