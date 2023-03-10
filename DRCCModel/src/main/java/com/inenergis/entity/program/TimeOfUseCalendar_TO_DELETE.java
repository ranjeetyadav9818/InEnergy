package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DayOfWeek;
import com.inenergis.entity.genericEnum.TimeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString(of = {"timeType","monthFrom","monthTo"})
@Entity
@Table(name = "TIME_OF_USE_CALENDAR_TO_DELETE")
public class TimeOfUseCalendar_TO_DELETE extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Column(name = "TIME_TYPE")
    @Enumerated(EnumType.STRING)
    private TimeType timeType;

    @Column(name = "MONTH_FROM")
    private Integer monthFrom;

    @Column(name = "MONTH_TO")
    private Integer monthTo;

    public TimeOfUseCalendar_TO_DELETE() {
    }

    public TimeOfUseCalendar_TO_DELETE(RatePlanProfile ratePlanProfile) {
        this.ratePlanProfile = ratePlanProfile;
    }
}