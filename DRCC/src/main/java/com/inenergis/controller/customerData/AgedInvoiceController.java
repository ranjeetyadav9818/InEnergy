package com.inenergis.controller.customerData;

import com.inenergis.controller.lazyDataModel.LazyInvoiceDataModel;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.genericEnum.AgedInvoiceCategory;
import com.inenergis.service.InvoiceService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class AgedInvoiceController implements Serializable {

    @Inject
    private InvoiceService invoiceService;

    private PieChartModel agedInvoicesPieChart;
    private MeterGaugeChartModel paidInvoicesGauge;

    private Double cashRealisationTime;
    private LazyInvoiceDataModel lazyUnpaidDueInvoices;

    @PostConstruct
    public void init() {
        loadAgedInvoicesPieChartData();
        loadPaidInvoicesGauge();
        loadCashRealisationTime();
    }

    private void loadAgedInvoicesPieChartData() {
        agedInvoicesPieChart = new PieChartModel();
        agedInvoicesPieChart.setLegendPosition("w");
        agedInvoicesPieChart.setSeriesColors("2196f3,bbdefb,ff5722,212121");
        agedInvoicesPieChart.setExtender("transparentBackgroundChartExtenderForPanel");

        AgedInvoiceCategory agedInvoiceCategory;
        Map<AgedInvoiceCategory, Long> agedInvoicesStat = new HashMap<>();

        for (Invoice invoice : invoiceService.getAllUnpaid()) {
            if (invoice.getDueDate().isAfter(LocalDate.now())) {
                agedInvoiceCategory = AgedInvoiceCategory.CURRENT;
            } else {
                long overdueDays = ChronoUnit.DAYS.between(invoice.getDueDate(), LocalDate.now());
                agedInvoiceCategory = AgedInvoiceCategory.getByOverdueDays(overdueDays);
            }
            agedInvoicesStat.putIfAbsent(agedInvoiceCategory, 0L);
            agedInvoicesStat.put(agedInvoiceCategory, agedInvoicesStat.get(agedInvoiceCategory) + 1L);
        }

        agedInvoicesStat.forEach((k, v) -> agedInvoicesPieChart.set(k.getName(), v));
    }

    private void loadPaidInvoicesGauge() {
        List<Invoice> paidInvoices = invoiceService.getAllPaid();
        Long totalPaidInvoices = (long) paidInvoices.size();
        Long totalPaidOnTimeInvoices = paidInvoices.stream()
                .filter(invoice -> invoice.getDueDate().isAfter(invoice.getPaymentDate().toLocalDate()))
                .count();

        final int percentOfPaidOnTimeInvoices;
        if (totalPaidInvoices > 0) {
            percentOfPaidOnTimeInvoices = (int) ((double) totalPaidOnTimeInvoices / (double) totalPaidInvoices * 100);
        } else {
            percentOfPaidOnTimeInvoices = 0;
        }

        List<Number> intervals = new ArrayList<Number>() {{
            add(0);
            add(percentOfPaidOnTimeInvoices);
            add(100);
        }};

        paidInvoicesGauge = new MeterGaugeChartModel(percentOfPaidOnTimeInvoices, intervals);
        paidInvoicesGauge.setSeriesColors("66cc66,BBDEFB,cc6666");
        paidInvoicesGauge.setExtender("mgExtender");
    }

    private void loadCashRealisationTime() {
        cashRealisationTime = invoiceService.getAllPaid().stream()
                .mapToLong(invoice -> ChronoUnit.DAYS.between(invoice.getDueDate(), invoice.getPaymentDate().toLocalDate()))
                .map(days -> Math.max(0, days))
                .average()
                .orElse(0);
    }
}
