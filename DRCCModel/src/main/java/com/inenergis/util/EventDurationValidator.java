package com.inenergis.util;

import com.inenergis.entity.Event;
import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramDefinedDispatchReason;
import com.inenergis.entity.program.ProgramEventDuration;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventDurationValidator {

    private Map<Program, EventHistoryHours> programEventHistoryHoursMap = new HashMap<>();

    public EventDurationValidator(IsoResource isoResource, Date tradeDate) {
        List<Program> programs = isoResource.getActiveRegistration().getLocations().stream()
                .map(LocationSubmissionStatus::getProgram)
                .collect(Collectors.toList());

        for (Program program : programs) {
            programEventHistoryHoursMap.put(program, new EventHistoryHours(isoResource, tradeDate, program));
        }
    }

    public boolean isLocationExhausted(LocationSubmissionStatus location) {
        return programEventHistoryHoursMap.get(location.getProgram()).isLocationExhausted(location);
    }

    public static class EventHistoryHours {
        private static final String MAX_YEAR_HOURS_REACHED = "Max Event Hours/Year limit is reached";
        private static final String MAX_MONTH_HOURS_REACHED = "Max Event Hours/Month limit is reached";
        private static final String MAX_DAY_HOURS_REACHED = "Max Event Hours/Day limit is reached";

        private Map<LocationSubmissionStatus, Long> yearHoursMap = new HashMap<>();
        private Map<LocationSubmissionStatus, Long> monthHoursMap = new HashMap<>();
        private Map<LocationSubmissionStatus, Long> dayHoursMap = new HashMap<>();
        private String lastError;

        EventHistoryHours(IsoResource isoResource, Date tradeDate, Program program) {
            List<DispatchReason> dispatchReasons = program.getActiveProfile().getDispatchReasons().stream()
                    .map(ProgramDefinedDispatchReason::getDispatchReason)
                    .collect(Collectors.toList());

            LocalDate localTradeDate = LocalDate.from(tradeDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
            List<ImpactedCustomer> impactedCustomers = isoResource.getImpactedResources().stream()
                    .map(ImpactedResource::getEvent)
                    .filter(event -> event.isStartDateOn(LocalDate.now().getYear()))
                    .filter(event -> event.getStatus().equals(EventStatus.SUBMITTED) || event.getStatus().equals(EventStatus.TERMINATED))
                    .filter(event -> dispatchReasons.contains(event.getDispatchReason()))
                    .map(Event::getImpactedCustomers)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            new EventHistoryHours(impactedCustomers, localTradeDate);
        }

        public EventHistoryHours(List<ImpactedCustomer> impactedCustomersTradeYear, LocalDate tradeDate) {
            yearHoursMap = impactedCustomersTradeYear.stream()
                    .collect(Collectors.groupingBy(ImpactedCustomer::getLocationSubmissionStatus,
                            Collectors.summingLong(ic -> ic.getEvent().getDuration().toHours())));

            monthHoursMap = impactedCustomersTradeYear.stream()
                    .filter(impactedCustomer -> impactedCustomer.getEvent().isStartDateOn(tradeDate.getYear(), tradeDate.getMonth()))
                    .collect(Collectors.groupingBy(ImpactedCustomer::getLocationSubmissionStatus,
                            Collectors.summingLong(ic -> ic.getEvent().getDuration().toHours())));

            dayHoursMap = impactedCustomersTradeYear.stream()
                    .filter(impactedCustomer -> impactedCustomer.getEvent().isStartDateOn(tradeDate))
                    .collect(Collectors.groupingBy(ImpactedCustomer::getLocationSubmissionStatus,
                            Collectors.summingLong(ic -> ic.getEvent().getDuration().toHours())));
        }

        private Long getYearHours(LocationSubmissionStatus locationSubmissionStatus) {
            return yearHoursMap.getOrDefault(locationSubmissionStatus, 0L);
        }

        private Long getMonthHours(LocationSubmissionStatus locationSubmissionStatus) {
            return monthHoursMap.getOrDefault(locationSubmissionStatus, 0L);
        }

        private Long getDayHours(LocationSubmissionStatus locationSubmissionStatus) {
            return dayHoursMap.getOrDefault(locationSubmissionStatus, 0L);
        }

        public boolean validateWithAdditionalHours(Long hours, LocationSubmissionStatus locationSubmissionStatus) {
            long hoursYear = hours + getYearHours(locationSubmissionStatus);
            long hoursMonth = hours + getMonthHours(locationSubmissionStatus);
            long hoursDay = hours + getDayHours(locationSubmissionStatus);
            ProgramEventDuration duration = locationSubmissionStatus.getProgram().getActiveProfile().getEventDurations().get(0);
            lastError = null;

            if (hoursYear > duration.getMaxEventHoursPerYear()) {
                lastError = MAX_YEAR_HOURS_REACHED;
                return false;
            }

            if (hoursMonth > duration.getMaxEventHoursPerMonth()) {
                lastError = MAX_MONTH_HOURS_REACHED;
                return false;
            }

            if (hoursDay > duration.getMaxEventHoursPerDay()) {
                lastError = MAX_DAY_HOURS_REACHED;
                return false;
            }

            return true;
        }

        boolean isLocationExhausted(LocationSubmissionStatus locationSubmissionStatus) {
            return !validateWithAdditionalHours(1L, locationSubmissionStatus);
        }

        public String getLastError() {
            return lastError;
        }
    }
}
