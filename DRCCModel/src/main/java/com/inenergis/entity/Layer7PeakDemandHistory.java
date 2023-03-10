package com.inenergis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LAYER7_PEAK_DEMAND_HISTORY")
public class Layer7PeakDemandHistory extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    private BaseServiceAgreement serviceAgreement;

    @Column(name = "MONTH_YEAR")
    private String monthYear;

    @Column(name = "MAX_KW_DEMAND")
    private String maxKwDemand;

    @Column(name = "SUMMER_ON_PEAK_KW")
    private String summerOnPeakKw;

    @Column(name = "SUMMER_ON_PEAK_KW_H")
    private String summerOnPeakKwH;

    @Column(name = "WINTER_PARTIAL_PEAK_KW")
    private String winterPartialPeakKw;

    @Column(name = "WINTER_PARTIAL_PEAK_KW_H")
    private String winterPartialPeakKwH;

    @Column(name = "SUMMER_ON_PEAK_HOURS")
    private String summerOnPeakHours;

    @Column(name = "WINTER_PARTIAL_PEAK_HOURS")
    private String winterPartialPeakHours;

    @Transient
    private LocalDate date;
}