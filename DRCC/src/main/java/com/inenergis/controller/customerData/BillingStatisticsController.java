package com.inenergis.controller.customerData;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.controller.lazyDataModel.LazyInvoiceDataModel;
import com.inenergis.controller.model.CycleSelectionTypeEnum;
import com.inenergis.entity.PaymentDetailedStatistics;
import com.inenergis.entity.PaymentSeriesStatistics;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.genericEnum.Month;
import com.inenergis.service.BillingCycleService;
import com.inenergis.service.InvoiceService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class BillingStatisticsController implements Serializable {

    public static final String TICK_FORMAT = "\\$%s ";
    @Inject
    private EntityManager entityManager;

    @Inject
    private InvoiceService invoiceService;

    @Inject
    private BillingCycleService billingCycleService;

    private PieChartModel paymentStatisticsPieChart;
    private BarChartModel paymentStatisticsBarChart;
    private List<PaymentDetailedStatistics> paymentDetailedStatistics;

    private List<Invoice> unpaidDueInvoices;
    private LazyInvoiceDataModel lazyInvoicesPerDay;
    private boolean showInvoices = false;

    private List<Long> years;

    final CycleSelectionTypeEnum[] cycleSelectionTypeEnums = CycleSelectionTypeEnum.values();

    private CycleSelectionTypeEnum cycleSelectionType = CycleSelectionTypeEnum.LAST_CYCLE;
    private int year;
    private Month month;
    private LocalDate now = LocalDate.now();

    @PostConstruct
    public void init() {
        year = now.getYear();
        month = Month.findByMonthNumber(now.getMonthValue());
        loadPaymentStatistics();
        years = new ArrayList<>();
        for (long i = 2017; i <= now.getYear(); i++) {
            years.add(i);
        }
    }

    private void loadPaymentStatistics() {
        List<PaymentSeriesStatistics> paymentSeriesStatistics = invoiceService.getStat(year,month.getValue());
        paymentStatisticsPieChart = new PieChartModel();
        paymentStatisticsPieChart.setLegendPosition("w");
        paymentStatisticsPieChart.setSeriesColors("2196f3,bbdefb,ff5722,212121");
        paymentStatisticsPieChart.setExtender("transparentBackgroundChartExtenderForPanel");
        final BigDecimal BD100 = BigDecimal.valueOf(100);
        paymentSeriesStatistics.forEach(ps -> {
                    BigDecimal total = BigDecimal.valueOf(ps.getTotal()).divide(BD100).setScale(2, RoundingMode.HALF_UP);
                    paymentStatisticsPieChart.set(ps.getSerial() + " - $" + total, total);
                }
        );

        paymentDetailedStatistics = invoiceService.getPaidUnpaidStat(year,month.getValue());
        final Map<LocalDate, List<PaymentDetailedStatistics>> statsByDate = paymentDetailedStatistics.stream().collect(Collectors.groupingBy(PaymentDetailedStatistics::getDate));
        paymentStatisticsBarChart = new BarChartModel();
        paymentStatisticsBarChart.setLegendPosition("nw");
        paymentStatisticsBarChart.setStacked(true);
        paymentStatisticsBarChart.setSeriesColors("2196f3,ffC107,ff5722,212121,bbdefb");
        paymentStatisticsBarChart.setExtender("transparentBackgroundChartExtenderForPanel");
        paymentStatisticsBarChart.setShowDatatip(false);
        paymentStatisticsBarChart.setMouseoverHighlight(false);
        paymentStatisticsBarChart.setBarWidth(10);
        paymentStatisticsBarChart.getAxes().put(AxisType.X, getDateAxis(LocalDateTime.now()));
        Axis yAxis = paymentStatisticsBarChart.getAxis(AxisType.Y);
        yAxis.setTickFormat(TICK_FORMAT);
        ChartSeries totalSeries = new ChartSeries("Unpaid");
        ChartSeries paidSeries = new ChartSeries("Paid");
        for (Map.Entry<LocalDate, List<PaymentDetailedStatistics>> entry : statsByDate.entrySet()) {
            final String dateFormatted = ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(entry.getKey().atStartOfDay().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant()));
            entry.getValue()
                    .stream()
                    .forEach(s -> {
                        final BigDecimal centsPaid = new BigDecimal(s.getTotal() - s.getUnpaidAmount());
                        final BigDecimal paid = centsPaid.divide(BD100).setScale(2, RoundingMode.HALF_UP);
                        final BigDecimal centsTotal = BigDecimal.valueOf(s.getTotal()).subtract(centsPaid);
                        final BigDecimal total = centsTotal.divide(BD100).setScale(2, RoundingMode.HALF_UP);
                        totalSeries.set(dateFormatted, total);
                        paidSeries.set(dateFormatted, paid);
                    });
        }
        paymentStatisticsBarChart.addSeries(paidSeries);
        paymentStatisticsBarChart.addSeries(totalSeries);

    }

    private DateAxis getDateAxis(LocalDateTime date) {
        DateAxis axis = new DateAxis();
        axis.setTickAngle(-50);
        axis.setMin(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.minusMonths(1).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setMax(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setTickFormat("%b %#d");
        axis.setTickInterval("1 day");
        axis.setTickCount(15);
        return axis;
    }

    public void onRowSelect(SelectEvent event) {
        PaymentDetailedStatistics paymentDetailedStatistics = (PaymentDetailedStatistics) event.getObject();
        Map<String, Object> filters = new HashMap<>();
        filters.put("date", paymentDetailedStatistics.getDate());
        lazyInvoicesPerDay = new LazyInvoiceDataModel(entityManager, filters);
        showInvoices = true;
    }

    public void loadCycle() {
        loadPaymentStatistics();
    }
}