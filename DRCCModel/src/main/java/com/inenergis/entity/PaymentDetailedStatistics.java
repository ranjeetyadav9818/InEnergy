package com.inenergis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class PaymentDetailedStatistics {

    @NonNull
    @Column(name = "serial")
    private String serial;

    @NonNull
    @Column(name = "date")
    private LocalDate date;

    @NonNull
    @Column(name = "count")
    private Long count;

    @NonNull
    @Column(name = "paid")
    private boolean paid;

    @Transient
    private int paidPercentage = 100;

    @Transient
    private long total = 0L;

    @Transient
    private long unpaidAmount = 0L;

    public void addUnpaid(long unpaid) {
        paidPercentage = (int) ((double) count / (double) (count + unpaid) * 100);
        count += unpaid;
    }
}