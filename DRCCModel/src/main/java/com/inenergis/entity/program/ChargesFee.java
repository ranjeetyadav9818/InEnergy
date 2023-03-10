package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.TimeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "CHARGES_FEES")
@NoArgsConstructor
public class ChargesFee extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "CHARGES_ATTRIBUTE_ID")
    private ChargesAttribute chargesAttribute;

    @ManyToOne
    @JoinColumn(name = "SEASON_ID")
    private SeasonCalendar season;

    @Column(name = "TIME_OF_USE")
    @Enumerated(EnumType.STRING)
    private TimeType timeOfUsePeriod;

    @Column(name = "TIME_OF_DAY_FROM")
    @Enumerated(EnumType.STRING)
    private HourEnd timeOfDayFrom;

    @Column(name = "TIME_OF_DAY_TO")
    @Enumerated(EnumType.STRING)
    private HourEnd timeOfDayTo;

    @Column(name = "EVENT_OPTION")
    @Enumerated(EnumType.STRING)
    private EventOption eventOption;

    @Column(name = "FEE_TYPE")
    @Enumerated(EnumType.STRING)
    private FeeType feeType;

    @Column(name = "FEE_FREQUENCY")
    @Enumerated(EnumType.STRING)
    private FeeFrequency feeFrequency;

    @Column(name = "FEE_AMOUNT")
    private BigDecimal feeAmount;

    @Column(name = "FEE_UNIT")
    @Enumerated(EnumType.STRING)
    private FeeableUnit feeUnit;

    @Column(name = "BASELINE")
    @Enumerated(EnumType.STRING)
    private FeeBaseline baseline;

    @Column(name = "BASELINE_FROM")
    private BigDecimal baselineFrom;

    @Column(name = "BASELINE_TO")
    private BigDecimal baselineTo;

    @Column(name = "BASELINE_UNIT")
    @Enumerated(EnumType.STRING)
    private FeeBaselineUnit baselineUnit;

    @Column(name = "COMPARISON_ELIGIBLE")
    private boolean comparisonEligible;

    public ChargesFee(ChargesAttribute attribute) {
        super();
        chargesAttribute = attribute;
    }
}