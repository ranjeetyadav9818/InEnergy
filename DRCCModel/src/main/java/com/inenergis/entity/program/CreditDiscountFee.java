package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CREDIT_DISCOUNT_FEE")
@NoArgsConstructor
public class CreditDiscountFee extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "CREDIT_DISCOUNT_ID")
    private CreditDiscount creditDiscount;

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

    //Removed @OneToMany to feeComparisonOne and feeComparisonTwo to avoid Multiple representations of the same entity problem

    public CreditDiscountFee(CreditDiscount creditDiscount) {
        super();
        this.creditDiscount = creditDiscount;
    }
}
