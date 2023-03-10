package com.inenergis.entity.bidding;


import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.genericEnum.DispatchForecastLevel;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.entity.workflow.ModifiableHourEnd;
import com.inenergis.util.EnergyUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ForecastHelper {

    public static void substractFsls(List<AggregableForecast> aggregableForecasts) {
        for (AggregableForecast forecastEnrollment : aggregableForecasts) {
            ProgramServiceAgreementEnrollment enrollment = forecastEnrollment.getEnrollment();
            final ProgramFirmServiceLevel fslByDate = enrollment.getFSLByDate((forecastEnrollment.getHourEndObject()).getMeasureDate());
            if (fslByDate != null) {
                final Long fslWatts = EnergyUtil.convertToWatts(fslByDate.getValue().longValue(), ElectricalUnit.KW);
                forecastEnrollment.substractToAllHourEnds(fslWatts);
            }
        }
    }

    public static void substractSafetyFactors(List<AggregableForecast> forecasts, Program program) {
        final ProgramProfile activeProfile = program.getActiveProfile();
        if (activeProfile == null || activeProfile.getSafetyReductionFactors() == null) {
            return;
        }
        for (AggregableForecast forecast : forecasts) {
            final LocationSubmissionStatus lastLocation = forecast.getEnrollment().getLastLocation();
            if (lastLocation != null) {
                final RegistrationSubmissionStatus lastRegistration = lastLocation.getLastRegistration();
                IsoResource lastResource = lastRegistration != null ? lastRegistration.getIsoResource() : null;
                final ModifiableHourEnd hourEndObject = forecast.getHourEndObject();
                final LocalDate measureDate = hourEndObject.getMeasureDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                for (int i = 1; i < 24; i++) {
                    hourEndObject.setHour(i, Math.max(0L, (long) hourEndObject.getHourEnd(i) - BidHelper.pickSafetyFactorByDayOfWeek(activeProfile, i, measureDate, lastResource)));
                }
            }
        }
    }

    public static List<AggregableForecast> average(Map<String, List<AggregableForecast>> groupedBySelection) {
        List<AggregableForecast> summarizedForecasts = new ArrayList<>();
        for (Map.Entry<String, List<AggregableForecast>> entry : groupedBySelection.entrySet()) {
            final List<AggregableForecast> forecastsToGroup = entry.getValue();
            AggregableForecast summarizedForecast = new AggregableForecast(new MeterForecast(), null);
            summarizedForecast.summarize(forecastsToGroup);
            summarizedForecasts.add(summarizedForecast);
            summarizedForecast.getHourEndObject().setUiName(entry.getKey());
        }
        return summarizedForecasts;
    }

    public static List<AggregableForecast> addEnrollments(List<MeterForecast> allMeterForecastsByProgram) {
        List<AggregableForecast> aggregableForecasts = new ArrayList();
        for (MeterForecast meterForecast : allMeterForecastsByProgram) {
            ProgramServiceAgreementEnrollment enrollment = BidHelper.getActiveEnrollment(meterForecast.getServiceAgreement());
            if (enrollment != null) {
                aggregableForecasts.add(new AggregableForecast(meterForecast, enrollment));
            }
        }

        return aggregableForecasts;
    }

    public static List<AggregableForecast> sum(Map<String, List<AggregableForecast>> groupedBySelection) {
        List<AggregableForecast> res = new ArrayList();
        for (Map.Entry<String, List<AggregableForecast>> entry : groupedBySelection.entrySet()) {
            final List<AggregableForecast> meterForecasts = entry.getValue();
            AggregableForecast aggregated = new AggregableForecast(new MeterForecast(), null);
            aggregated.add(meterForecasts);
            aggregated.getHourEndObject().setUiName(entry.getKey());
            res.add(aggregated);
        }
        return res;
    }

    public static void addToMap(Map<String, List<AggregableForecast>> forecastsGroupedBy, AggregableForecast meterForecast, String key) {
        List<AggregableForecast> meterForecasts = forecastsGroupedBy.get(key);
        if (meterForecasts == null) {
            meterForecasts = new ArrayList<>();
            meterForecasts.add(meterForecast);
            forecastsGroupedBy.put(key, meterForecasts);
        } else {
            meterForecasts.add(meterForecast);
        }
    }

    public static Map<String, List<AggregableForecast>> collectForecastsByUserSelection(List<AggregableForecast> forecastsWithEnrollment, DispatchForecastLevel dispatchLevel) {
        Map<String, List<AggregableForecast>> forecastsGroupedBy;
        // Assumption made: a service point has only one agreement point map
        switch (dispatchLevel) {
            case FEEDER:
                forecastsGroupedBy = forecastsWithEnrollment.stream().collect(Collectors.groupingBy(f -> unknownIfNull(((ServicePoint)f.getEnrollment().getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint()).getFeeder())));
                break;
            case SUBSTATION:
                forecastsGroupedBy = forecastsWithEnrollment.stream().collect(Collectors.groupingBy(f -> unknownIfNull(((ServicePoint)f.getEnrollment().getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint()).getSubstation())));
                break;
            case SUBLAP:
            case RESOURCE:
                forecastsGroupedBy = new HashMap();
                for (AggregableForecast aggregableForecast : forecastsWithEnrollment) {
                    ProgramServiceAgreementEnrollment enrollment = aggregableForecast.getEnrollment();
                    String key;
                    if (enrollment == null || enrollment.getLastLocation() == null || enrollment.getLastLocation().getActiveResource() == null) {
                        continue;
                    }
                    if (DispatchForecastLevel.SUBLAP.equals(dispatchLevel)) {
                        key = enrollment.getLastLocation().getActiveResource().getIsoSublap ();
                    } else {
                        key = enrollment.getLastLocation().getActiveResource().getName();
                    }
                    addToMap(forecastsGroupedBy, aggregableForecast, key);
                }
                break;
            default:
                forecastsGroupedBy = new HashMap();
                break;
        }
        forecastsGroupedBy.entrySet().forEach(fs -> fs.getValue().forEach(f -> f.getHourEndObject().setUiName(fs.getKey())));
        return forecastsGroupedBy;
    }

    public static String unknownIfNull(String value) {
        if (value != null) {
            return value;
        }
        return "UNKNOWN";
    }
}
