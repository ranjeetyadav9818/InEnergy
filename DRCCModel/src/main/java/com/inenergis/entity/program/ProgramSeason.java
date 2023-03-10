package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "profile")
@Entity
@Table(name = "PROGRAM_SEASON")
public class ProgramSeason extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Column(name = "DISPATCH_MON")
    private boolean dispatchMonday;

    @Column(name = "DISPATCH_TUE")
    private boolean dispatchTuesday;

    @Column(name = "DISPATCH_WED")
    private boolean dispatchWednesday;

    @Column(name = "DISPATCH_THU")
    private boolean dispatchThursday;

    @Column(name = "DISPATCH_FRI")
    private boolean dispatchFriday;

    @Column(name = "DISPATCH_SAT")
    private boolean dispatchSaturday;

    @Column(name = "DISPATCH_SUN")
    private boolean dispatchSunday;


    @Column(name = "MONTH_FROM")
    private Integer monthFrom;

    @Column(name = "DAY_FROM")
    private Integer dayFrom;

    @Column(name = "MONTH_TO")
    private Integer monthTo;

    @Column(name = "DAY_TO")
    private Integer dayTo;

    @Column(name = "OPERATING_HOUR_FROM")
    private Integer operatingHourFrom;

    @Column(name = "OPERATING_MINUTE_FROM")
    private Integer operatingMinuteFrom;

    @Column(name = "OPERATING_HOUR_TO")
    private Integer operatingHourTo;

    @Column(name = "OPERATING_MINUTE_TO")
    private Integer operatingMinuteTo;

    public ProgramSeason() {
    }

    public ProgramSeason(ProgramProfile programProfile) {
        this.profile = programProfile;
    }

    @Transient
    private List<String> selectedDays = new ArrayList<>();

    @PostLoad
    public void onLoad() {
        if (dispatchMonday) {
            selectedDays.add("Mon");
        }
        if (dispatchTuesday) {
            selectedDays.add("Tue");
        }
        if (dispatchWednesday) {
            selectedDays.add("Wed");
        }
        if (dispatchThursday) {
            selectedDays.add("Thu");
        }
        if (dispatchFriday) {
            selectedDays.add("Fri");
        }
        if (dispatchSaturday) {
            selectedDays.add("Sat");
        }
        if (dispatchSunday) {
            selectedDays.add("Sun");
        }
    }

    public void onCreate() {
        dispatchMonday = selectedDays.contains("Mon");
        dispatchTuesday = selectedDays.contains("Tue");
        dispatchWednesday = selectedDays.contains("Wed");
        dispatchThursday = selectedDays.contains("Thu");
        dispatchFriday = selectedDays.contains("Fri");
        dispatchSaturday = selectedDays.contains("Sat");
        dispatchSunday = selectedDays.contains("Sun");
    }

    public boolean notFilledIn() {
        return (StringUtils.isEmpty(name) && monthFrom == null && monthTo == null);
    }

    public boolean isDispatchOn(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return isDispatchMonday();
            case TUESDAY:
                return isDispatchTuesday();
            case WEDNESDAY:
                return isDispatchWednesday();
            case THURSDAY:
                return isDispatchThursday();
            case FRIDAY:
                return isDispatchFriday();
            case SATURDAY:
                return isDispatchSaturday();
            case SUNDAY:
                return isDispatchSunday();
        }

        return false;
    }

    public Date getStartDate() {
        if (monthFrom != null &&
                dayFrom != null &&
                operatingHourFrom != null &&
                operatingMinuteFrom != null) {
            LocalDateTime ldtFrom = LocalDateTime.of(LocalDateTime.now().getYear(), monthFrom, dayFrom, operatingHourFrom, operatingMinuteFrom);
            return Date.from(ldtFrom.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        } else {
            return null;
        }
    }

    public Date getEndDate() {
        if (monthTo != null &&
                dayTo != null &&
                operatingHourTo != null &&
                operatingMinuteTo != null) {
            LocalDateTime ldtFrom = LocalDateTime.of(LocalDateTime.now().getYear(), monthTo, dayTo, operatingHourTo, operatingMinuteTo);
            return Date.from(ldtFrom.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        } else {
            return null;
        }
    }

    public void setOperatingTimeFrom(LocalTime hour) {
        if (hour != null) {
            operatingHourFrom = hour.getHour();
            operatingMinuteFrom = hour.getMinute();
        } else {
            operatingHourFrom = null;
            operatingMinuteFrom = null;
        }
    }

    public LocalTime getOperatingTimeFrom() {
        if (operatingHourFrom == null || operatingMinuteFrom == null) {
            return null;
        }
        return LocalTime.of(operatingHourFrom, operatingMinuteFrom);
    }

    public void setOperatingTimeTo(LocalTime hour) {
        if (hour != null) {
            operatingHourTo = hour.getHour();
            operatingMinuteTo = hour.getMinute();
        } else {
            operatingHourTo = null;
            operatingMinuteTo = null;
        }
    }

    public LocalTime getOperatingTimeTo() {
        if (operatingHourTo == null || operatingMinuteTo == null) {
            return null;
        }
        return LocalTime.of(operatingHourTo, operatingMinuteTo);
    }
}