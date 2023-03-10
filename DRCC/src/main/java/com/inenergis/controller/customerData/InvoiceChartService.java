package com.inenergis.controller.customerData;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.service.InvoiceService;
import com.inenergis.service.aws.DateGroupedIntervalData;
import com.inenergis.service.aws.IntervalDataService;
import com.inenergis.service.aws.PeakDemandDataService;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LinearAxis;
import org.primefaces.model.chart.PieChartModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Stateless

public class InvoiceChartService {

    @Inject
    InvoiceService invoiceService;

    @Inject
    IntervalDataService intervalDataService;

    @Inject
    PeakDemandDataService peakDemandDataService;

    public BarChartModel renderMonthlyBillingHistoryChart(String serviceAgreementId, LocalDateTime date) throws IOException, SQLException {
        ChartSeries charges = new ChartSeries();
        final ImmutablePair minMax = buildAnnualTotalSeries(charges, serviceAgreementId, date);
        BarChartModel monthlyBillingHistoryChart = new BarChartModel();
        monthlyBillingHistoryChart.addSeries(charges);
        monthlyBillingHistoryChart.setTitle("Monthly Billing History");
        monthlyBillingHistoryChart.setLegendPosition("ne");
        monthlyBillingHistoryChart.setStacked(true);
        monthlyBillingHistoryChart.setSeriesColors("2196f3,bbdefb,ff5722,212121");

        Axis yAxis = monthlyBillingHistoryChart.getAxis(AxisType.Y);
        yAxis.setLabel("Amount");
        yAxis.setTickFormat(ConstantsProvider.MONEY_TIP_FORMAT);
        yAxis.setMin(minMax.getLeft());
        yAxis.setMax(minMax.getRight());
        monthlyBillingHistoryChart.setShowDatatip(false);
        monthlyBillingHistoryChart.setShowPointLabels(true);
        monthlyBillingHistoryChart.setExtender("transparentBackgroundChartExtender");
        monthlyBillingHistoryChart.setBarWidth(10);
        monthlyBillingHistoryChart.getAxes().put(AxisType.X, getYearlyAxis(date));

        return monthlyBillingHistoryChart;
    }

    public BarChartModel renderElectricUsageThisPeriodChart(BaseServiceAgreement serviceAgreement, LocalDateTime date) throws IOException, SQLException {

       final List<DateGroupedIntervalData> intervalDataGroupByDay = intervalDataService.getIntervalDataGroupByDay(serviceAgreement.getAgreementPointMaps(), date.minusMonths(1), date);
       if (CollectionUtils.isEmpty(intervalDataGroupByDay)) {
           return null;
       }
       final Map<String, List<DateGroupedIntervalData>> dataGroupedBySP = intervalDataGroupByDay.stream().collect(Collectors.groupingBy(DateGroupedIntervalData::getServicePointId, Collectors.toList()));
       final BigDecimal max = intervalDataGroupByDay.stream().map(i -> i.getUsageValue()).reduce(BigDecimal.ZERO, BigDecimal::max).setScale(2, BigDecimal.ROUND_HALF_UP);
       final BigDecimal min = intervalDataGroupByDay.stream().map(i -> i.getUsageValue()).reduce(BigDecimal.ZERO, BigDecimal::min).setScale(2, BigDecimal.ROUND_HALF_UP);
        BarChartModel electricUsagePeriod = new BarChartModel();
        for (List<DateGroupedIntervalData> spSeries : dataGroupedBySP.values()) {
           electricUsagePeriod.addSeries(buildCompsumptionSeriesOnAMonth(spSeries, date));
       }

       electricUsagePeriod.addSeries(buildAverageCompsumption(intervalDataGroupByDay, date.toLocalDate()));
        electricUsagePeriod.setTitle("Electric Usage This Period");
        electricUsagePeriod.setLegendPosition("ne");
        electricUsagePeriod.setSeriesColors("2196f3,bbdefb,8bc34a,212121,ff5722,1976d2");
        electricUsagePeriod.setExtender("transparentBackgroundChartExtender");
        electricUsagePeriod.setBarWidth(10);
        electricUsagePeriod.setStacked(true);
        Axis yAxis = electricUsagePeriod.getAxis(AxisType.Y);
        yAxis.setLabel("Energy");
        yAxis.setTickFormat("%sKw/h");
       yAxis.setMax(max.multiply(new BigDecimal(dataGroupedBySP.size())));
       yAxis.setMin(min);
        electricUsagePeriod.setShowDatatip(true);
        electricUsagePeriod.setShowPointLabels(false);

        DateAxis axis = new DateAxis("Days");
        axis.setTickAngle(-50);
        axis.setMin(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.minusMonths(1).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setMax(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setTickFormat("%b %#d");
        axis.setTickInterval("day");
        axis.setTickCount(15);
        electricUsagePeriod.getAxes().put(AxisType.X, axis);
        return electricUsagePeriod;
    }

    private LineChartSeries buildAverageCompsumption(List<DateGroupedIntervalData> consumption, LocalDate date) {
        if (CollectionUtils.isEmpty(consumption)) {
            return null;
        }
        LineChartSeries average = new LineChartSeries();
        average.setShowMarker(false);
        average.setLabel("Avg KW/h");
        average.setDisableStack(true);
        BigDecimal averageDay = BigDecimal.ZERO;
        if (consumption != null) {
            final BigDecimal total = consumption.stream().map(c -> c.getUsageValue()).reduce(BigDecimal.ZERO, BigDecimal::add);
            averageDay = total.divide(new BigDecimal(consumption.size()), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        }
        initOneMonthSeriesToValue(date, average, averageDay);
        return average;
    }

    private void initOneMonthSeriesToValue(LocalDate date, ChartSeries average, BigDecimal finalAverage) {
        for (LocalDate seriesDate = date.minusMonths(1); seriesDate.isBefore(date); seriesDate = seriesDate.plusDays(1)) {
            average.set(
                    ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(seriesDate.getYear(), Integer.valueOf(seriesDate.getMonthValue()), Integer.valueOf(seriesDate.getDayOfMonth()), 0, 0)
                            .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())),
                    finalAverage);
        }
    }

    private ImmutablePair buildAnnualTotalSeries(ChartSeries charges, String serviceAgreementId, LocalDateTime date) {
        final List<Invoice> invoicesSaYear = invoiceService.getByServiceAgreementYear(serviceAgreementId, LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
        charges.setLabel("Total");
        List<LocalDate> monthsOfYear = getMonthsOfYearFromCurrent(date);
        if (CollectionUtils.isNotEmpty(invoicesSaYear)) {
            final Map<LocalDate, List<Invoice>> invoicesByMonth = invoicesSaYear.stream().collect(Collectors.groupingBy(l -> LocalDate.of(l.getDateTo().getYear(), l.getDateTo().getMonth(), 1)));
            monthsOfYear.forEach(l ->
            {
                List<Invoice> invoices = invoicesByMonth.get(LocalDate.of(l.getYear(), l.getMonth(), 1));
                if (CollectionUtils.isEmpty(invoices)) {
                    invoices = new ArrayList<>();
                }
                final Long charge = invoices.stream()
                        .map(i -> i.getTotal())
                        .reduce(0L, (t1, t2) -> t1 + t2);
                final BigDecimal scaledCharge = new BigDecimal(charge).divide(new BigDecimal(100), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                charges.set(
                        ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(l.getYear(), l.getMonth(), l.getDayOfMonth(), 0, 0)
                                .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())), scaledCharge);
            });
        } else {
            monthsOfYear.forEach(l -> {
                charges.set(
                        ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(l.getYear(), l.getMonth(), l.getDayOfMonth(), 0, 0)
                                .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            });
        }
        final BigDecimal min = charges.getData().values().stream().map(c -> (BigDecimal) c).reduce(BigDecimal.ZERO, (c1, c2) -> c1.min(c2));
        final BigDecimal max = charges.getData().values().stream().map(c -> (BigDecimal) c).reduce(BigDecimal.ONE, (c1, c2) -> c1.max(c2));
        return new ImmutablePair(min, max);
    }

    private List<LocalDate> getMonthsOfYearFromCurrent(LocalDateTime date) {
        List<LocalDate> monthsOfYear = new ArrayList<>();
        final LocalDate localDate = date.toLocalDate();
        final LocalDate nextMonth = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1).plusMonths(1);
        for (LocalDate monthToShow = nextMonth.minusYears(1).plusMonths(1); monthToShow.isBefore(nextMonth); monthToShow = monthToShow.plusMonths(1)) {
            monthsOfYear.add(monthToShow);
        }
        return monthsOfYear;
    }

    private ChartSeries buildCompsumptionSeriesOnAMonth(List<DateGroupedIntervalData> intervalDataGroupByDaySp, LocalDateTime date) throws IOException, SQLException {
        ChartSeries consumptionSeries = new ChartSeries();
        if (CollectionUtils.isEmpty(intervalDataGroupByDaySp)) {
            return consumptionSeries;
        }
        final int year = date.getYear();
        final String servicePointId = intervalDataGroupByDaySp.get(0).getServicePointId();
        consumptionSeries.setLabel(servicePointId);
        initOneMonthSeriesToValue(date.toLocalDate(), consumptionSeries, BigDecimal.ZERO);
        intervalDataGroupByDaySp.stream().forEach(i ->
        {
            final BigDecimal consumption = i.getUsageValue().setScale(2, RoundingMode.HALF_UP);
            consumptionSeries.set(
                    ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(year, Integer.valueOf(i.getMonth()), Integer.valueOf(i.getDay()), 0, 0)
                            .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())),
                    consumption.setScale(2, BigDecimal.ROUND_HALF_UP));
        });
        return consumptionSeries;
    }

    private void initMonthlyConsumptionSeries(ChartSeries consumptionSeries, LocalDate date, BigDecimal value) {
        for (LocalDate seriesDate = date.minusYears(1).plusMonths(1); seriesDate.isBefore(date); seriesDate = seriesDate.plusMonths(1)) {
            consumptionSeries.set(
                    ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(seriesDate.getYear(), seriesDate.getMonthValue(), 1, 0, 0)
                            .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())), value);
        }
    }

    private ChartSeries generateSeriesForSp(LocalDateTime dateTo, int year, List<DateGroupedIntervalData> spData) {
        if (CollectionUtils.isEmpty(spData)) {
            return null;
        }
        ChartSeries consumptionSeries = new ChartSeries();
        final String servicePointId = spData.get(0).getServicePointId();
        consumptionSeries.setLabel("KWh: " + servicePointId);
        initMonthlyConsumptionSeries(consumptionSeries, dateTo.toLocalDate(), BigDecimal.ZERO);
        spData.stream().forEach(i ->
        {
            final BigDecimal consumption = i.getUsageValue();
            consumptionSeries.set(
                    ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(year, Integer.valueOf(i.getMonth()), 1, 0, 0)
                            .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())),
                    consumption);
        });
        return consumptionSeries;
    }

    public BarChartModel renderMonthlyBillingHistoryConsumptionChart(BaseServiceAgreement serviceAgreement, LocalDateTime date) throws IOException, SQLException {
       final List<DateGroupedIntervalData> intervalDataGroupByMonth = intervalDataService.getIntervalDataGroupByMonth(serviceAgreement.getAgreementPointMaps(), date.minusYears(1).withDayOfMonth(1), date);
        BarChartModel monthlyBillingHistoryChart = new BarChartModel();
        int year = date.getYear();
       addIntervalDataSeries(serviceAgreement, date, intervalDataGroupByMonth, monthlyBillingHistoryChart, year);
       final List<DateGroupedIntervalData> peakDemandGroupByMonth = peakDemandDataService.getIntervalDataGroupByMonth(serviceAgreement.getAgreementPointMaps(), date.minusYears(1).withDayOfMonth(1), date);
       final Map<String, List<DateGroupedIntervalData>> dataGroupedBySP = peakDemandGroupByMonth.stream().collect(Collectors.groupingBy(DateGroupedIntervalData::getServicePointId, Collectors.toList()));
//
       dataGroupedBySP.entrySet().forEach(sp -> {
           monthlyBillingHistoryChart.addSeries(buildPeakDemandLine(sp.getValue(), year, sp.getKey()));
       });
        monthlyBillingHistoryChart.setTitle("Monthly Consumption History");
        monthlyBillingHistoryChart.setLegendPosition("ne");
        monthlyBillingHistoryChart.setStacked(true);
        monthlyBillingHistoryChart.setSeriesColors("ff5722,2196f3,212121,bbdefb,ffffff,0b8793,360033");

       final BigDecimal min = intervalDataGroupByMonth.stream().map(id -> id.getUsageValue()).reduce(BigDecimal.ZERO, ((c1, c2) -> c1.min(c2)));
       final BigDecimal max = intervalDataGroupByMonth.stream().map(id -> id.getUsageValue()).reduce(BigDecimal.ONE, ((c1, c2) -> c1.max(c2)));

       buildEnergyAxis(date, monthlyBillingHistoryChart, min, max, serviceAgreement);
        final DateAxis yearlyAxisX2 = getConsumptionDemandYearlyAxis(date, " ", "");
        monthlyBillingHistoryChart.getAxes().put(AxisType.X2, yearlyAxisX2);

       buildDemandAxis(monthlyBillingHistoryChart, peakDemandGroupByMonth, max);

        monthlyBillingHistoryChart.setShowDatatip(false);
        monthlyBillingHistoryChart.setShowPointLabels(false);
        monthlyBillingHistoryChart.setExtender("transparentBackgroundChartExtender");
        monthlyBillingHistoryChart.setBarWidth(10);


        return monthlyBillingHistoryChart;
    }

    private void buildDemandAxis(BarChartModel monthlyBillingHistoryChart, List<DateGroupedIntervalData> peakDemandGroupByMonth, BigDecimal max) {
        Axis y2Axis = new LinearAxis("Demand");
        final BigDecimal minY2 = peakDemandGroupByMonth.stream().map(id -> id.getUsageValue()).reduce(BigDecimal.ZERO, ((c1, c2) -> c1.min(c2)));
        final BigDecimal maxY2 = peakDemandGroupByMonth.stream().map(id -> id.getUsageValue()).reduce(BigDecimal.ONE, ((c1, c2) -> c1.max(c2)));
        final BigDecimal totalMax = maxY2.max(max);
        if (totalMax.compareTo(BigDecimal.ONE) <= 0) {
            y2Axis.setTickFormat("%sKw");
            y2Axis.setMin(minY2);
            y2Axis.setMax(totalMax);
        } else {
            y2Axis.setTickFormat("%dKw");
        }
        monthlyBillingHistoryChart.getAxes().put(AxisType.Y2, y2Axis);
    }

    private void buildEnergyAxis(LocalDateTime date, BarChartModel monthlyBillingHistoryChart, BigDecimal min, BigDecimal max, BaseServiceAgreement serviceAgreement) {

        Axis yAxis = monthlyBillingHistoryChart.getAxis(AxisType.Y);
        yAxis.setLabel("Consumption");

        if(serviceAgreement.getDecriminatorValue().toString().equals("Electricity")){

            if (max.compareTo(BigDecimal.ONE) <= 0) {
                yAxis.setTickFormat("%sKw/h");
                yAxis.setMin(min);
                yAxis.setMax(max);
            } else {
                yAxis.setTickFormat("%dKw/h");
            }
            monthlyBillingHistoryChart.getAxes().put(AxisType.X, getConsumptionDemandYearlyAxis(date,null,null));

        }else{

            if (max.compareTo(BigDecimal.ONE) <= 0) {
                yAxis.setTickFormat("%sTherms/h");
                yAxis.setMin(min);
                yAxis.setMax(max);
            } else {
                yAxis.setTickFormat("%dTherms/h");
            }
            monthlyBillingHistoryChart.getAxes().put(AxisType.X, getConsumptionDemandYearlyAxis(date,null,null));

        }

    }

    private void addIntervalDataSeries(BaseServiceAgreement serviceAgreement, LocalDateTime date, List<DateGroupedIntervalData> intervalDataGroupByMonth, BarChartModel monthlyBillingHistoryChart, int year) {
        if (CollectionUtils.isNotEmpty(intervalDataGroupByMonth)) {
            final Map<String, List<DateGroupedIntervalData>> dataGroupedBySP = intervalDataGroupByMonth.stream().collect(Collectors.groupingBy(DateGroupedIntervalData::getServicePointId, Collectors.toList()));
            dataGroupedBySP.values().forEach(sp -> {
                monthlyBillingHistoryChart.addSeries(generateSeriesForSp(date, year, sp));
            });
        } else {
            if (serviceAgreement.getAgreementPointMaps() != null) {
                for (AgreementPointMap agreementPointMap : serviceAgreement.getAgreementPointMaps()) {
                    monthlyBillingHistoryChart.addSeries(generateEmptyMonthlySeries(date, agreementPointMap.getServicePoint().getServicePointId()));
                }
            } else {
                monthlyBillingHistoryChart.addSeries(generateEmptyMonthlySeries(date, StringUtils.EMPTY));
            }
        }
    }

    private ChartSeries generateEmptyMonthlySeries(LocalDateTime date, String servicePointId) {
        final ChartSeries consumptionSeries = new ChartSeries();
        consumptionSeries.setLabel("SP : " + servicePointId);
        initMonthlyConsumptionSeries(consumptionSeries, date.toLocalDate(), BigDecimal.ZERO.setScale(2));
        return consumptionSeries;
    }

    private LineChartSeries buildPeakDemandLine(List<DateGroupedIntervalData> peakDemandData, int year, String servicePointId) {
        if (CollectionUtils.isEmpty(peakDemandData)) {
            return null;
        }
        LineChartSeries lineChart = new LineChartSeries();
        lineChart.setShowMarker(false);
        lineChart.setLabel("KW:&nbsp;&nbsp;&nbsp;" + servicePointId);
        lineChart.setDisableStack(true);
        lineChart.setXaxis(AxisType.X2);
        lineChart.setYaxis(AxisType.Y2);
        peakDemandData.stream().forEach(i -> {
            lineChart.set(
                    ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(LocalDateTime.of(year, Integer.valueOf(i.getMonth()), i.getDay() != null ? Integer.valueOf(i.getDay()) : 28, 0, 0)
                            .atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())),
                    i.getUsageValue().setScale(2, BigDecimal.ROUND_HALF_UP));
        });
        return lineChart;
    }

    public PieChartModel renderFeesPieChart(Invoice invoice) {
        PieChartModel pieChartModel = new PieChartModel();
        pieChartModel.setTitle("Billing Detail");
        pieChartModel.setSeriesColors("8bc34a,ff5722,2196f3,ffC107,0D47A1,bbdefb,212121");
        pieChartModel.setShowDataLabels(true);
        pieChartModel.setShowDatatip(true);
        pieChartModel.setExtender("transparentBackgroundChartExtender");
        pieChartModel.setLegendPosition("w");
        pieChartModel.setDatatipFormat("%s\\: \\$%s");
        Map<String, List<InvoiceLine>> collect = invoice.getInvoiceLines()
                .stream().filter(l -> StringUtils.isNotEmpty(l.getConcept()))
                .collect(Collectors.groupingBy(InvoiceLine::getConcept));

        collect.entrySet().forEach(feeStats -> pieChartModel.set(feeStats.getKey(), new BigDecimal(feeStats.getValue().stream().mapToLong(InvoiceLine::getTotal).sum()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)));


        return pieChartModel;
    }

    private DateAxis getYearlyAxis(LocalDateTime date) {
        DateAxis axis = new DateAxis("Months");
        axis.setTickAngle(-50);
        axis.setMin(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.minusYears(1).minusMonths(1).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setMax(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.plusMonths(1).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setTickFormat("%b '%y");
        axis.setTickInterval("1 month");
        return axis;
    }

    private DateAxis getConsumptionDemandYearlyAxis(LocalDateTime date, String tickFormat, String label) {
        DateAxis axis = new DateAxis(label == null ? "Months" : label);
        axis.setTickAngle(-50);
        axis.setMin(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.minusYears(1).minusMonths(1).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setMax(ConstantsProvider.DATE_AXIS_FORMATTER_FOR_CHARTS.format(Date.from(date.plusMonths(3).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant())));
        axis.setTickFormat(tickFormat == null ? "%b '%y" : tickFormat);
        axis.setTickInterval("1 month");
        return axis;
    }
}
