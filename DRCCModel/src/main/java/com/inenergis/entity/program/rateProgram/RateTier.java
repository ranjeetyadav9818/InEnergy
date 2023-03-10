package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.*;
import com.inenergis.entity.program.RatePlanProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = "name")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@Table(name = "RATE_TIER")
@Entity
public abstract class RateTier extends IdentifiableEntity implements Comparable{

    @Column(name = "NAME")
    private String name;

    @Column(name = "PENALTY_AMOUNT")
    private BigDecimal penaltyAmount;

    @Column(name = "PENALTY_PERIOD")
    @Enumerated(EnumType.STRING)
    private TierPenaltyPeriod penaltyPeriod;

    @Column(name = "FROM_BOUND")
    @Enumerated(EnumType.STRING)
    private TierBoundType fromBound;

    @Column(name = "FROM_AMOUNT_VALUE")
    private BigDecimal fromAmountValue;

    @Column(name = "FROM_FORMULA_VARIABLE")
    @Enumerated(EnumType.STRING)
    private TierVariableType fromFormulaVariable;

    @Column(name = "GAS_FROM_FORMULA_VARIABLE")
    @Enumerated(EnumType.STRING)
    private GasTierVariableType gasfromFormulaVariable;

    @Column(name = "FROM_FORMULA_OPERATOR")
    @Enumerated(EnumType.STRING)
    private TierOperatorType fromFormulaOperator;


    @Column(name = "TO_BOUND")
    @Enumerated(EnumType.STRING)
    private TierBoundType toBound;

    @Column(name = "TO_AMOUNT_VALUE")
    private BigDecimal toAmountValue;

    @Column(name = "TO_FORMULA_VARIABLE")
    @Enumerated(EnumType.STRING)
    private TierVariableType toFormulaVariable;

    @Column(name = "TO_FORMULA_OPERATOR")
    @Enumerated(EnumType.STRING)
    private TierOperatorType toFormulaOperator;

    @Column(name = "ACTIVE")
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Transient
    private final LocalDateTime creationTime = LocalDateTime.now();

    @Override
    public int compareTo(Object o) {
        RateTier other = (RateTier) o;
        if (this.getId() != null) {
            if (other.getId() != null) {
                return this.getId().compareTo(other.getId());
            } else {
                return 1; //I have id and the other not. I am superior
            }
        } else if (other.getId() != null) {
            return -1; //I do not have id. so I am inferior
        } else { // No ids to compare (both not persistent objects). Compare creation dates
            return this.getCreationTime().compareTo(other.getCreationTime());
        }
    }
}
