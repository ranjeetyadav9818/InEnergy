package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "BILLING_CYCLE_SCHEDULE")
@ToString
public class BillingCycleSchedule extends IdentifiableEntity {

    @Column(name = "BILLING_DATE")
    private LocalDate date;

    @Column(name = "SERIAL")
    private String serial;

    @Column(name = "SENT")
    private boolean sent;
}