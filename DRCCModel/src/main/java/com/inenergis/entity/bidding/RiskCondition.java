package com.inenergis.entity.bidding;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.RelationalOperator;
import com.inenergis.entity.genericEnum.RiskCategory;
import com.inenergis.entity.genericEnum.RiskSource;
import com.inenergis.entity.genericEnum.RiskTarget;
import com.inenergis.entity.marketIntegration.IsoProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = "profile")
@Entity
@Table(name = "RISK_CONDITION")
public class RiskCondition extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "MI_ISO_PROFILE_ID")
    private IsoProfile profile;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    RiskCategory category;

    @Column(name = "SOURCE")
    @Enumerated(EnumType.STRING)
    RiskSource source;

    @Column(name = "COMPARISON_OPERATOR")
    @Enumerated(EnumType.STRING)
    RelationalOperator comparisonOperator;

    @Column(name = "TARGET")
    @Enumerated(EnumType.STRING)
    RiskTarget target;

    @Column(name = "CUSTOM_VALUE")
    BigDecimal customValue;

    @Transient
    BigDecimal sourceValue;

    @Transient
    BigDecimal targetValue;

    @Transient
    boolean pending = false;

    public void setSourceValue(BigDecimal value) {
        sourceValue = value;
        pending = true;
    }

    public void setTargetValue(BigDecimal value) {
        targetValue = value;
        pending = true;
    }

    public boolean evaluate() {
        pending = false;

        if (sourceValue == null || targetValue == null) {
            return false;
        }

        switch (comparisonOperator) {
            case GE:
                return sourceValue.compareTo(targetValue) > 0 || Objects.equals(sourceValue, targetValue);
            case LE:
                return sourceValue.compareTo(targetValue) < 0 || Objects.equals(sourceValue, targetValue);
            case EQ:
                return Objects.equals(sourceValue, targetValue);
            case GT:
                return sourceValue.compareTo(targetValue) > 0;
            case LT:
                return sourceValue.compareTo(targetValue) < 0;
            case NE:
                return !Objects.equals(sourceValue, targetValue);
            default:
                return false;
        }
    }
}