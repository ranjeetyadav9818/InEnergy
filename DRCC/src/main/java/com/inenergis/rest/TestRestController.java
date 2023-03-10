package com.inenergis.rest;

import com.inenergis.dao.MeterDataUsageDao;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.MeterDataUsage;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesRequest;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesResponse;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsRequest;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsResponse;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsRequest;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsResponse;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorRequest;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorResponse;
import com.inenergis.rest.services.EventsAndOutagesRESTService;
import com.inenergis.rest.services.RegistrationDetailRESTService;
import com.inenergis.rest.services.ResourceDetailsRESTService;
import com.inenergis.rest.services.VoltageIndicatorRESTService;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.service.TemperatureForecastService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.joda.time.Days;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Stateless
@Path("/test")
public class TestRestController {

    SimpleDateFormat sdf = new SimpleDateFormat("'of' MMM yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
    @Inject
    private ServiceAgreementService serviceAgreementService;
    @Inject
    private TemperatureForecastService temperatureForecastService;
    @Inject
    private MeterDataUsageDao meterDataUsageDao;

    Logger log = LoggerFactory.getLogger(TestRestController.class);


    @GET
    @Path("/forecast")
    @Produces(MediaType.APPLICATION_JSON)
    public ForecastSummary getForecast(){
        ZonedDateTime now = ZonedDateTime.now();
        ForecastSummary forecastSummary = new ForecastSummary();
        for (int i = 0; i <5; i++) {
            forecastSummary.getTemperature()[i] = temperatureForecastService.getByDate(Date.from(now.plusDays(i).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())).getDegrees().setScale(0,BigDecimal.ROUND_UP);
        }
        forecastSummary.setAdditionalMessage("There is a 65% chance of calling an event in "+ getMaxDay(forecastSummary) +" days time");
        return forecastSummary;
    }

    private Integer getMaxDay(ForecastSummary forecastSummary) {
        BigDecimal max = new BigDecimal(-10_000L);
        int maxDay = 0;
        int currentDay = 0;
        for (BigDecimal t : forecastSummary.getTemperature()) {
            if(t.compareTo(max) > 0){
                max = t;
                maxDay = currentDay;
            }
            currentDay++;
        }
        return maxDay;
    }

    @GET
    @Path("/getSA/{said}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceAgreementSummary getServiceAgreement(@PathParam("said") String said){
        BaseServiceAgreement serviceAgreement = serviceAgreementService.getById(said);
        ServiceAgreementSummary summary = new ServiceAgreementSummary();
        summary.setName(serviceAgreement.getAccount().getPerson().getCustomerName().replace(",",", "));
        List<MeterDataUsage> allByServicePoint = meterDataUsageDao.getAllByServicePoint(serviceAgreement.getAgreementPointMaps().get(0).getServicePoint().getServicePointId());
        BigDecimal total = BigDecimal.ZERO;
        for (MeterDataUsage meterDataUsage : allByServicePoint) {
            total = total.add(meterDataUsage.getPrice());
        }
        summary.setCurrentBalance(total);
        Date date = new Date((new Date()).getTime() - 3600000000L);
        summary.setLastPaymentDone(calculateLastPaymentDate(date));
        if (!allByServicePoint.isEmpty()){
            summary.setLastReadingTimeAgo(calculateLastReading(allByServicePoint.get(allByServicePoint.size()-1).getDate()));
        }
        return summary;
    }

    private String calculateLastPaymentDate(Date date) {
        int day = Integer.parseInt(sdf2.format(date));
        return day + getDayOfMonthSuffix(day) + " " + sdf.format(date);
    }

    String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    private String calculateLastReading(Date lastPaymentDone) {
        ZonedDateTime ldt = lastPaymentDone.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime now = ZonedDateTime.now();
        long days = ChronoUnit.DAYS.between(ldt, now);
        if(days > 0){
            if(days==1L){
                return "1 day ago";
            } else {
                return days + " days ago";
            }
        }else{
            long hours = ChronoUnit.HOURS.between(ldt, now);
            if(hours>0){
                if(hours==1L){
                    return "1 hour ago";
                } else {
                    return hours + " hours ago";
                }
            }else{
                long minutes = ChronoUnit.MINUTES.between(ldt, now);
                if(minutes>0){
                    if(minutes==1L){
                        return "1 minute ago";
                    } else {
                        return minutes + " minutes ago";
                    }
                }else{
                    return "just a moment ago";
                }
            }
        }
    }

    @Getter
    @Setter
    public class ServiceAgreementSummary{
        private String name;
        private BigDecimal currentBalance;
        private String lastPaymentDone;
        private String lastReadingTimeAgo;
    }

    @Getter
    @Setter
    private class ForecastSummary {
        private BigDecimal[] temperature = new BigDecimal[5];
        private String additionalMessage;
    }
}