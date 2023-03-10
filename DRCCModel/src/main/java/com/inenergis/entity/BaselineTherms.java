package com.inenergis.entity;

import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "BASELINE_THERMS")
public class BaselineTherms extends IdentifiableEntity {
    @Column(name = "VALUE")
    private String code;

    @Column(name = "BILLING_DATE")
    private LocalDate localDate;

}