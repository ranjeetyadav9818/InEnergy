package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DemandType;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.RelationalOperator;
import lombok.Getter;
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
@Table(name = "RATE_PLAN_DEMAND")
public class RatePlanDemand extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @Column(name = "CALCULATION_INDICATOR")
    private String calculationIndicator;

    @Column(name = "CALCULATION_VALUE")
    private String calculationValue;

    @Column(name = "VALUE")
    private BigDecimal value;

    @Column(name = "UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit unit;

    @Column(name = "COMPARATOR")
    @Enumerated(EnumType.STRING)
    private RelationalOperator relationalOperator;

    @Column(name = "DEMAND_TYPE")
    @Enumerated(EnumType.STRING)
    private DemandType demandType;

    @Column(name = "TIME_HORIZON")
    private Integer timeHorizon;

    @Column(name = "PRIOR_MONTHS")
    private Integer priorMonths;

    @Column(name = "CONSECUTIVE")
    private boolean consecutive;

    public RatePlanDemand() {
    }

    public RatePlanDemand(RatePlanProfile ratePlanProfile) {
        this.ratePlanProfile = ratePlanProfile;
    }
}