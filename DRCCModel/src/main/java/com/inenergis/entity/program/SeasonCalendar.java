package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "SEASON_CALENDAR")
public class SeasonCalendar extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MONTH_FROM")
    private Integer monthFrom;

    @Column(name = "DAY_FROM")
    private Integer dayFrom;

    @Column(name = "MONTH_TO")
    private Integer monthTo;

    @Column(name = "DAY_TO")
    private Integer dayTo;

    public SeasonCalendar() {
    }

    public SeasonCalendar(RatePlanProfile ratePlanProfile) {
        this.ratePlanProfile = ratePlanProfile;
    }
}