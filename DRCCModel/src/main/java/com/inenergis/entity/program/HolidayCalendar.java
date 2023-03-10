package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DayOfWeek;
import com.inenergis.entity.genericEnum.WeekOrdinalNumber;
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
import java.util.Date;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "HOLIDAY_CALENDAR")
public class HolidayCalendar extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MONTH")
    private Integer month;

    @Column(name = "FIXED_DATE")
    private Date fixedDate;

    @Column(name = "VARIABLE_DATE")
    private Date variableDate;

    @Column(name = "WEEK_ORDINAL_NUMBER")
    @Enumerated(EnumType.STRING)
    private WeekOrdinalNumber weekOrdinalNumber;

    @Column(name = "DAY_OF_WEEK")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    public HolidayCalendar() {
    }

    public HolidayCalendar(RatePlanProfile ratePlanProfile) {
        this.ratePlanProfile = ratePlanProfile;
    }
}