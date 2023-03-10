package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.FslRuleComparisonReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM_FSL_RULE")
public class ProgramFirmServiceLevelRule extends IdentifiableEntity {

    public static final String NON_ESSENTIAL = "Non-Essential";
    public static String ESSENTIAL = "Essential";

    @ManyToOne
    @JoinColumn(name = "PROFILE_ID")
    private ProgramProfile profile;
    @JoinColumn(name = "SEASON_ID")
    @ManyToOne
    private ProgramSeason season;
    @Column(name = "ESSENTIAL")
    private boolean essential;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "COMPARISON_ATTRIBUTE")
    private String comparisonAttribute;
    @Column(name = "COMPARISON_OPERATOR")
    private String comparisonOperator;
    @Column(name = "COMPARISON_VALUE")
    private BigDecimal comparisonValue;

    @Column(name = "COMPARISON_REFERENCE")
    @Enumerated(EnumType.STRING)
    private FslRuleComparisonReference comparisonReference;

    @Column(name = "ENERGY_UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit energyUnit;

    @Column(name = "NEXT_RULE_LINK")
    private String nextRuleLink;

    @Transient
    private String seasonUuid;

    @Override
    public String toString() {
        return " " + (comparisonAttribute == null ? "" : comparisonAttribute) +
                " " + comparisonOperator +
                " " + comparisonValue +
                " " + (comparisonReference == null ? "" : comparisonReference) +
                " " + (energyUnit == null ? "" : energyUnit.getName()) +
                " " + (season == null ? "" : "on " + season.getName()) +
                " " + (nextRuleLink == null ? "" : nextRuleLink) +
                ". Essential: " + essential;
    }
}