package com.inenergis.entity.masterCalendar;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.TimeOfUseDayType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "TIME_OF_USE_DAY")
public class TimeOfUseDay extends IdentifiableEntity {
    @Column(name = "DAY")
    @Enumerated(EnumType.STRING)
    private TimeOfUseDayType day;

    @ManyToOne
    @JoinColumn(name = "TIME_OF_USE_ID")
    private TimeOfUse timeOfUse;

    public TimeOfUseDay() {
    }

    public TimeOfUseDay(TimeOfUseDayType day, TimeOfUse timeOfUse) {
        this.day = day;
        this.timeOfUse = timeOfUse;
    }
}