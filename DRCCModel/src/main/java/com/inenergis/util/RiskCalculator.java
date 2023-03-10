package com.inenergis.util;

import com.inenergis.entity.CapacityRisk;
import com.inenergis.entity.Event;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.RiskCondition;
import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.RiskCategory;
import com.inenergis.entity.genericEnum.RiskType;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.ProgramDefinedDispatchReason;
import com.inenergis.entity.program.ProgramProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RiskCalculator {

    protected static final Logger log = LoggerFactory.getLogger(RiskCalculator.class);


    private List<Event> eventsThisYear;
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public CapacityRisk validateCapacity(Bid bid) {
        List<RiskCondition> riskConditions = bid.getIsoResource().getIsoProduct().getProfile().getRiskConditions();
        CapacityRisk result = new CapacityRisk();
        for (RiskCondition riskCondition : riskConditions) {
            if (riskCondition.getCategory().equals(RiskCategory.CAPACITY)) {
                validateCapacity(riskCondition, bid, result);
            }
        }
        return result;
    }

    public RiskType validateFatigue(Bid bid, List<Event> eventsThisYear) {
        return validateWithEvents(bid, eventsThisYear, RiskCategory.FATIGUE);
    }

    public RiskType validateProgram(Bid bid, List<Event> eventsThisYear) {
        return validateWithEvents(bid, eventsThisYear, RiskCategory.PROGRAM);
    }

    private RiskType validateWithEvents(Bid bid, List<Event> eventsThisYear, RiskCategory riskCategory) {
        final ProgramProfile activeProfile = bid.getSegments().get(0).getProgram().getActiveProfile();
            if (activeProfile != null) {

            List<DispatchReason> dispatchReasons = activeProfile.getDispatchReasons().stream()
                    .map(ProgramDefinedDispatchReason::getDispatchReason)
                    .collect(Collectors.toList());

            this.eventsThisYear = eventsThisYear.stream()
                    .filter(dr -> dispatchReasons.contains(dr.getDispatchReason()))
                    .collect(Collectors.toList());

            List<RiskCondition> riskConditions = bid.getIsoResource().getIsoProduct().getProfile().getRiskConditions().stream()
                    .filter(c -> c.getCategory().equals(riskCategory)).collect(Collectors.toList());

            for (RiskCondition riskCondition : riskConditions) {
                if (!evaluateRules(riskCondition, bid)) {
                    return RiskType.High;
                }
            }
        }
        return RiskType.Low;
    }

    private CapacityRisk validateCapacity(RiskCondition riskCondition, Bid bid, CapacityRisk capacityRisk) {
        for (int i = 0; i < 24; i++) {
            capacityRisk.getCapacityValid().set(i, capacityRisk.getCapacityValid().get(i) && evaluateRules(riskCondition, bid, i));
        }
        return capacityRisk;
    }

    private BigDecimal getSourceValue(RiskCondition riskCondition, Bid bid, Integer hour) {
        Long result;
        switch (riskCondition.getSource()) {
            case FORECASTED_FSL:
                result = bid.getSegments().get(1).getForecastedCapacitiessAsList().get(hour);
                break;
            case P_MIN:
                result = 1_000_000L * (long) bid.getIsoResource().getActivePmaxPmin().getPmin();
                break;
            case P_MAX:
                result = 1_000_000L * (long) bid.getIsoResource().getActivePmaxPmin().getPmax();
                break;
            case CONSECUTIVE_DISPATCH_DAYS_THRESHOLD:
                return getConsecutiveDaysDispatchLocationsComplyingRatio(bid, bid.getTradeLocalDate());
            case MAX_HOURS_ACHIEVED:
                return getMaxHoursAchievedLocationsComplyingRatio(bid);
            default:
                return null;
        }

        return BigDecimal.valueOf(result);
    }

    private BigDecimal getTargetValue(RiskCondition riskCondition, Bid bid, Integer hour) {
        Long result;

        switch (riskCondition.getTarget()) {
            case FORECASTED_FSL:
                result = bid.getSegments().get(1).getForecastedCapacitiessAsList().get(hour);
                break;
            case P_MIN:
                result = 1_000_000L * (long) bid.getIsoResource().getActivePmaxPmin().getPmin();
                break;
            case P_MAX:
                result = 1_000_000L * (long) bid.getIsoResource().getActivePmaxPmin().getPmax();
                break;
            case CONSECUTIVE_DISPATCH_DAYS_THRESHOLD:
                return getConsecutiveDaysDispatchLocationsComplyingRatio(bid, bid.getTradeLocalDate());
            case MAX_HOURS_ACHIEVED:
                return getMaxHoursAchievedLocationsComplyingRatio(bid);
            case CUSTOM:
                return riskCondition.getCustomValue();
            case CUSTOM_PERCENTAGE:
                return riskCondition.getCustomValue()
                        .multiply(riskCondition.getSourceValue())
                        .divide(ONE_HUNDRED, 2, BigDecimal.ROUND_CEILING);
            default:
                return null;
        }

        return BigDecimal.valueOf(result);
    }

    private BigDecimal getMaxHoursAchievedLocationsComplyingRatio(Bid bid) {
        List<LocationSubmissionStatus> locations = bid.getRegistration().getLocations();
        int totalLocations = locations.size();
        int locationsComplying = 0;
        Short maxEventHoursPerMonth = bid.getSegments().get(0).getProgram().getActiveProfile().getEventDurations().get(0).getMaxEventHoursPerMonth();
        Short maxEventHoursPerYear = bid.getSegments().get(0).getProgram().getActiveProfile().getEventDurations().get(0).getMaxEventHoursPerYear();
        for (LocationSubmissionStatus location : locations) {
            Long locationEventHoursThisMonth = getEventHoursThisMonth(location, bid.getTradeDate());
            Long locationEventHoursThisYear = getEventHoursThisYear(location);
            if (locationEventHoursThisMonth < maxEventHoursPerMonth && locationEventHoursThisYear < maxEventHoursPerYear) {
                locationsComplying++;
            }
            log.info("location {}: {}, {}, {}, {}, {}, {}", location.getProgramServiceAgreementEnrollment().getServiceAgreement().getServiceAgreementId(), locationEventHoursThisMonth, maxEventHoursPerMonth, locationEventHoursThisYear, maxEventHoursPerYear, locationsComplying, totalLocations);
        }
        return new BigDecimal(locationsComplying).divide(new BigDecimal(totalLocations), 3, BigDecimal.ROUND_HALF_UP);

    }

    private BigDecimal getConsecutiveDaysDispatchLocationsComplyingRatio(Bid bid, LocalDate tradeLocalDate) {
        List<LocationSubmissionStatus> locations = bid.getRegistration().getLocations();
        int totalLocations = locations.size();
        int locationsComplying = 0;
        Integer consecutiveDispatchDaysThreshold = bid.getSegments().get(0).getProgram().getActiveProfile().getConsecutiveDispatchDays();
        if (consecutiveDispatchDaysThreshold == null) {
            return new BigDecimal(1);
        }
        for (LocationSubmissionStatus location : locations) {
            long consecutiveDaysForCurrentLocation = getConsecutiveDays(bid, tradeLocalDate, location);
            if (consecutiveDaysForCurrentLocation < consecutiveDispatchDaysThreshold) {
                locationsComplying++;
            }
        }
        return new BigDecimal(locationsComplying).divide(new BigDecimal(totalLocations), 3, BigDecimal.ROUND_HALF_UP);
    }

    private long getConsecutiveDays(Bid bid, LocalDate tradeDate, LocationSubmissionStatus location) {
        List<ImpactedResource> impactedResources = bid.getIsoResource().getImpactedResources().stream()
                .filter(ir -> ir.getEvent().isStartDateBefore(tradeDate) && ir.getEvent().containsLocation(location))
                .sorted(Comparator.comparing(ir -> ir.getEvent().getStartDate(), Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        long consecutiveDays = 0;
        LocalDate testDate = tradeDate.minusDays(1);
        for (ImpactedResource impactedResource : impactedResources) {
            if (impactedResource.getEvent().isStartDateOn(testDate)) {
                consecutiveDays++;
                testDate = testDate.minusDays(1);
            } else {
                break;
            }
        }

        return consecutiveDays;
    }

    private Long getEventHoursThisYear(LocationSubmissionStatus location) {
        return eventsThisYear.stream()
                .filter(e -> e.containsLocation(location))
                .mapToLong(event -> Duration.between(event.getStartDate().toInstant(), event.getEndDate().toInstant()).toHours())
                .sum();
    }

    private Long getEventHoursThisMonth(LocationSubmissionStatus location, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return eventsThisYear.stream()
                .filter(e -> e.isStartDateOn(localDate.getYear(), localDate.getMonth()))
                .filter(e -> e.containsLocation(location))
                .mapToLong(event -> Duration.between(event.getStartDate().toInstant(), event.getEndDate().toInstant()).toHours())
                .sum();
    }

    private boolean evaluateRules(RiskCondition riskCondition, Bid bid) {
        return evaluateRules(riskCondition, bid, null);
    }

    private boolean evaluateRules(RiskCondition riskCondition, Bid bid, Integer hour) {
        riskCondition.setSourceValue(getSourceValue(riskCondition, bid, hour));
        riskCondition.setTargetValue(getTargetValue(riskCondition, bid, hour));

        return riskCondition.evaluate();
    }
}