package com.inenergis.entity.masterCalendar;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "TIME_OF_USE_HOUR")
@NoArgsConstructor
public class TimeOfUseHour extends IdentifiableEntity {
    @Column(name = "HOUR")
    private Integer hour;

    @ManyToOne
    @JoinColumn(name = "TIME_OF_USE_ID")
    private TimeOfUse timeOfUse;

    public TimeOfUseHour(int hour, TimeOfUse timeOfUse) {
        this.hour = hour;
        this.timeOfUse = timeOfUse;
    }
}