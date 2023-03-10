package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.EventDurationOption;
import com.inenergis.entity.genericEnum.MinutesOrHours;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM_EVENT_DURATION")
public class ProgramEventDuration extends IdentifiableEntity {

    @Column(name = "EVENT_DURATION_OPTION")
    @Enumerated(EnumType.STRING)
    private EventDurationOption option;

    @Column(name = "MIN_DURATION")
    private Short minDuration;

    @Column(name = "MAX_DURATION")
    private Short maxDuration;

    @Column(name = "MIN_DURATION_UNITS")
    @Enumerated(EnumType.STRING)
    private MinutesOrHours minDurationUnits;

    @Column(name = "MAX_DURATION_UNITS")
    @Enumerated(EnumType.STRING)
    private MinutesOrHours maxDurationUnits;

    @Column(name = "MAX_EVENT_HOURS_PER_YEAR")
    private Short maxEventHoursPerYear;

    @Column(name = "MAX_EVENT_HOURS_PER_MONTH")
    private Short maxEventHoursPerMonth;

    @Column(name = "MAX_EVENT_HOURS_PER_DAY")
    private Short maxEventHoursPerDay;

    @Column(name = "MIN_LEAD_TIME_TO_SHED_LOAD")
    private Short minLeadTimeToShedLoad;

    @Column(name = "MIN_LEAD_TIME_TO_SHED_LOAD_UNITS")
    @Enumerated(EnumType.STRING)
    private MinutesOrHours minLeadTimeToShedLoadUnits;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PRODUCT_ID")
    private ProgramProduct product;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        return "{" +
                "option=" + option +
                ", min duration=" + minDuration +
                ", max duration=" + maxDuration +
                ", min duration Units=" + minDurationUnits +
                ", max duration Units=" + maxDurationUnits +
                ", max event hours per year=" + maxEventHoursPerYear +
                ", max event hours per month=" + maxEventHoursPerMonth +
                ", max event hours per day=" + maxEventHoursPerDay +
                ", min lead time to shed load=" + minLeadTimeToShedLoad +
                ", min lead time to shed load units=" + minLeadTimeToShedLoadUnits +
                '}';
    }
    @Transient
    private List<ProgramProduct> optionProducts;
}