package com.inenergis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSeriesStatistics {

    @Column(name = "serial")
    private String serial;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total")
    private Long total;

    @Column(name = "unpaid")
    private Long unpaid;
}

