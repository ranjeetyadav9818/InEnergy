package com.inenergis.controller.dispatch;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.bidding.AggregableForecast;
import com.inenergis.entity.bidding.BidHelper;
import com.inenergis.entity.bidding.ForecastHelper;
import com.inenergis.entity.genericEnum.DispatchForecastFormat;
import com.inenergis.entity.genericEnum.DispatchForecastLevel;
import com.inenergis.entity.genericEnum.DispatchForecastType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.service.MeterForecastService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class ForecastDispatchController implements Serializable {

    @Inject
    private UIMessage uiMessage;

    @Inject
    private ProgramServiceContract programService;

    @Inject
    private SubLapService subLapService;

    @Inject
    EntityManager entityManager;

    @Inject
    MeterForecastService meterForecastService;

    private Program program;
    private List<Program> programs;

    private DispatchForecastLevel dispatchLevel;


    private DispatchForecastFormat forecastFormat;


    private DispatchForecastType dispatchForecastType;


    private Date dateFrom;

    private Integer numberOfDays;

    private List<Pair<Date, List<AggregableForecast>>> heRows = new ArrayList();

    @PostConstruct
    public void init() {
        programs = programService.getPrograms();
    }

    public void calculate() {
        heRows =  new ArrayList();
        final LocalDateTime lDateFrom = dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
        final LocalDateTime lDateTo = LocalDateTime.from(lDateFrom).plusDays(numberOfDays);
        Date dateTo = Date.from(lDateTo.atZone(ZoneId.systemDefault()).toInstant());
        final List<BaseServiceAgreement> readyServiceAgreements = BidHelper.getReadyServiceAgreements(programService.getActiveSAs(program));

        String measureType = MeterForecast.REFERENCCE_LOAD;

        final List<MeterForecast> allMeterForecastsByProgram = meterForecastService.getBy(readyServiceAgreements, measureType, dateFrom, dateTo);

        if (CollectionUtils.isEmpty(allMeterForecastsByProgram)) {
            uiMessage.addMessage("No records found", FacesMessage.SEVERITY_INFO);
            return;
        }

        final List<AggregableForecast> aggregableForecasts = ForecastHelper.addEnrollments(allMeterForecastsByProgram);

        ForecastHelper.substractFsls(aggregableForecasts);

        if (DispatchForecastType.ADJUSTED_FORECAST.equals(dispatchForecastType)) {
            ForecastHelper.substractSafetyFactors(aggregableForecasts, program);
        }

        if (DispatchForecastFormat.SUMMARIZED.equals(forecastFormat)) {
            calculateSummarizedTable(aggregableForecasts);
        } else {
            //Group by measureDate
            final Map<LocalDateTime, List<AggregableForecast>> groupedByDate = aggregableForecasts.stream().collect(Collectors.groupingBy(
                    f -> f.getHourEndObject().getMeasureDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()));

            calculateMeasureDateTables(groupedByDate);
        }
    }

    private void calculateMeasureDateTables(Map<LocalDateTime, List<AggregableForecast>> groupedByDate) {
        for (Map.Entry<LocalDateTime, List<AggregableForecast>> entry : groupedByDate.entrySet()) {
            final Map<String, List<AggregableForecast>> groupedBySelection = ForecastHelper.collectForecastsByUserSelection(entry.getValue(), dispatchLevel);
            final Date uiDate = Date.from(entry.getKey().atZone(ZoneId.systemDefault()).toInstant());
            final List<AggregableForecast> sumGrouped = ForecastHelper.sum(groupedBySelection);
            Pair pair = new ImmutablePair(uiDate, sumGrouped);
            heRows.add(pair);
        }
    }

    private void calculateSummarizedTable(List<AggregableForecast> aggregableForecasts) {
        final Map<String, List<AggregableForecast>> groupedBySelection = ForecastHelper.collectForecastsByUserSelection(aggregableForecasts, dispatchLevel);
        final List<AggregableForecast> sumGrouped = ForecastHelper.average(groupedBySelection);
        heRows.add(new ImmutablePair(dateFrom, sumGrouped));
    }
}
