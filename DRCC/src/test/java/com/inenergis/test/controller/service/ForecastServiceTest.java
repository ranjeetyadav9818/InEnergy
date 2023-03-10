package com.inenergis.test.controller.service;


import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.bidding.AggregableForecast;
import com.inenergis.entity.bidding.ForecastHelper;
import com.inenergis.entity.bidding.SafetyReductionFactorHe;
import com.inenergis.entity.genericEnum.DispatchForecastLevel;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastServiceTest {

    public static final long DEFAULT_HOUR_END_VALUE = 1000L;
    
    @Test
    public void testSubstractFsls() throws Exception {

        final ProgramServiceAgreementEnrollment enrollmentEmptyFsl = Mockito.mock(ProgramServiceAgreementEnrollment.class);
        enrollmentEmptyFsl.setFsls(null);

        final ProgramServiceAgreementEnrollment enrollmentFsl = new ProgramServiceAgreementEnrollment();
        ProgramFirmServiceLevel fsl = new ProgramFirmServiceLevel();
        final ZonedDateTime startDate = ZonedDateTime.now().minusDays(7);
        final Date endDate = new Date();
        fsl.setEffectiveStartDate(Date.from(startDate.toInstant()));
        fsl.setValue(new BigDecimal(1)); //kw
        enrollmentFsl.setFsls(Arrays.asList(fsl));
        final MeterForecast forecastWithDate = new MeterForecast();
        forecastWithDate.setMeasureDate(endDate);
        forecastWithDate.setHourEnd1(3000); //watt
        final AggregableForecast aggregableForecast = new AggregableForecast(forecastWithDate, enrollmentFsl);
        List<AggregableForecast> aggregableForecasts = Arrays.asList(
                new AggregableForecast(Mockito.mock(MeterForecast.class), enrollmentEmptyFsl),
                aggregableForecast
        );
        ForecastHelper.substractFsls(aggregableForecasts);
        Assertions.assertEquals(forecastWithDate.getHourEnd1(),2000);
    }

    @Test
    public void testSubstractSafetyFactors() throws Exception {
        Program program = new Program();
        ProgramProfile profile = new ProgramProfile();
        final SafetyReductionFactorHe he = new SafetyReductionFactorHe();
        he.setHourEnd(HourEnd.HE_1);
        long SAFETY_FACTOR = 100L;
        he.setMonday(SAFETY_FACTOR);
        he.setTuesday(SAFETY_FACTOR);
        he.setWednesday(SAFETY_FACTOR);
        he.setThursday(SAFETY_FACTOR);
        he.setFriday(SAFETY_FACTOR);
        he.setSaturday(SAFETY_FACTOR);
        he.setSunday(SAFETY_FACTOR);
        he.setProgramHoliday(SAFETY_FACTOR);
        profile.setSafetyReductionFactors(Arrays.asList(
                he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he, he
        ));
        profile.setEffectiveEndDate(null);
        profile.setEffectiveStartDate(new Date());
        program.setProfiles(Arrays.asList(profile));

        ProgramServiceAgreementEnrollment enrollment = getEnrollmentLocationResourceRegistration();

        final MeterForecast forecast = new MeterForecast();
        forecast.setMeasureDate(new Date());
        forecast.setHourEnd1(SAFETY_FACTOR);
        List<AggregableForecast> aggregableForecasts = Arrays.asList(
                new AggregableForecast(forecast, enrollment)
        );
        ForecastHelper.substractSafetyFactors(aggregableForecasts, program);
        Assertions.assertEquals(forecast.getHourEnd1(),0L);
    }


    @Test
    public void testAverage() {
        Map<String, List<AggregableForecast>> map = getAggregableForecastMap();
        final List<AggregableForecast> average = ForecastHelper.average(map);
        Assertions.assertEquals(average.size(), 2);
        final Long hourEnd = (Long) average.get(0).getHourEndObject().getHourEnd(1);
        Assertions.assertEquals(hourEnd.longValue(),DEFAULT_HOUR_END_VALUE);
    }

    @Test
    public void testAddEnrollments() {
        MeterForecast forecast = getForecastWithEnrollment();
        final List<AggregableForecast> result = ForecastHelper.addEnrollments(Arrays.asList(forecast));
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertNotNull(result.get(0));
    }


    @Test
    public void testSum() {
        Map<String, List<AggregableForecast>> map = getAggregableForecastMap();
        final List<AggregableForecast> sum = ForecastHelper.sum(map);
        Assertions.assertEquals(sum.size(), 2);
        final Long hourEnd = (Long) sum.get(0).getHourEndObject().getHourEnd(1);
        Assertions.assertEquals(hourEnd.longValue(),DEFAULT_HOUR_END_VALUE * 2);
    }

    @Test
    public void testAddToMap() {
        Map<String, List<AggregableForecast>> map = new HashMap();
        AggregableForecast forecast = new AggregableForecast(Mockito.mock(MeterForecast.class), Mockito.mock(ProgramServiceAgreementEnrollment.class));
        ForecastHelper.addToMap(map, forecast, "SUBSTATION1");
        Assertions.assertEquals(map.size(), 1);
        ForecastHelper.addToMap(map, forecast, "SUBSTATION1");
        ForecastHelper.addToMap(map, forecast, "SUBSTATION1");
        ForecastHelper.addToMap(map, forecast, "SUBSTATION2");
        ForecastHelper.addToMap(map, forecast, "SUBSTATION2");
        Assertions.assertEquals(map.size(), 2);
    }

    @Test
    public void testCollectForecastsByUserSelection() {
        MeterForecast forecast = getForecastWithEnrollment();
        AggregableForecast aggregableForecast = new AggregableForecast(forecast, forecast.getServiceAgreement().getEnrollments().get(0));
        final Map<String, List<AggregableForecast>> map1 = ForecastHelper.collectForecastsByUserSelection(Arrays.asList(aggregableForecast), DispatchForecastLevel.FEEDER);
        final Map<String, List<AggregableForecast>> map2 = ForecastHelper.collectForecastsByUserSelection(Arrays.asList(aggregableForecast), DispatchForecastLevel.SUBSTATION);
        final Map<String, List<AggregableForecast>> map3 = ForecastHelper.collectForecastsByUserSelection(Arrays.asList(aggregableForecast), DispatchForecastLevel.RESOURCE);
        Assertions.assertEquals(map1.size(), 1);
        Assertions.assertEquals(map2.size(), 1);
        Assertions.assertEquals(map3.size(), 1);
    }

    private MeterForecast getForecastWithEnrollment() {
        ServiceAgreement sa = getServiceAgreement();
        final ProgramServiceAgreementEnrollment enrollment = getEnrollmentLocationResourceRegistration();
        enrollment.setServiceAgreement(sa);
        sa.setEnrollments(Arrays.asList(enrollment));
        MeterForecast forecast = new MeterForecast();
        forecast.setServiceAgreement(sa);
        return forecast;
    }

    private ServiceAgreement getServiceAgreement() {
        ServiceAgreement sa = new ServiceAgreement();
        AgreementPointMap apm = new AgreementPointMap();
        ServicePoint sp = new ServicePoint();
        sp.setFeeder("FEEDER1");
        sp.setSubstation("SUBSTATION1");
        apm.setServicePoint(sp);
        apm.setServiceAgreement(sa);
        sa.setAgreementPointMaps(Arrays.asList(apm));
        return sa;
    }

    private ProgramServiceAgreementEnrollment getEnrollmentLocationResourceRegistration() {
        ProgramServiceAgreementEnrollment enrollment = new ProgramServiceAgreementEnrollment();
        LocationSubmissionStatus location = new LocationSubmissionStatus();
        RegistrationSubmissionStatus registration = new RegistrationSubmissionStatus();
        IsoResource resource = new IsoResource();
        resource.setIsoSublap ("SUBLAP1");
        resource.setName("ResourceName1");
        registration.setIsoResource(resource);
        registration.setActiveStartDate(new Date());
        final ZonedDateTime endDate = ZonedDateTime.now().plusDays(7);
        registration.setActiveEndDate(Date.from(endDate.toInstant()));
        registration.setRegistrationStatus(RegistrationSubmissionStatus.RegistrationStatus.REGISTERED);
        location.setRegistrations(Arrays.asList(registration));
        enrollment.setLocations(Arrays.asList(location));
        location.setProgramServiceAgreementEnrollment(enrollment);
        return enrollment;
    }

    private Map<String, List<AggregableForecast>> getAggregableForecastMap() {
        final MeterForecast hourEndObject = new MeterForecast();
        hourEndObject.setHourEnd1(DEFAULT_HOUR_END_VALUE);
        final AggregableForecast aggregableForecast = new AggregableForecast(hourEndObject, Mockito.mock(ProgramServiceAgreementEnrollment.class));
        List<AggregableForecast> aggregableForecasts = Arrays.asList(
                aggregableForecast,
                aggregableForecast
        );
        Map<String, List<AggregableForecast>> map = new HashMap();
        map.put("SUBSTATION1", aggregableForecasts);
        map.put("SUBSTATION2", aggregableForecasts);
        return map;
    }

}
