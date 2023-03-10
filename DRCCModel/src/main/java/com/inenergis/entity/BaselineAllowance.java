package com.inenergis.entity;

import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "BASELINE_ALLOWANCE")
public class BaselineAllowance extends IdentifiableEntity {
    @Column(name = "CODE")
    private String code;

    @ManyToOne
    @JoinColumn(name = "TIME_OF_USE_CALENDAR_ID")
    private TimeOfUseCalendar timeOfUseCalendar;

    @Column(name = "KWH_PER_DAY")
    private BigDecimal kwhPerDay;
}