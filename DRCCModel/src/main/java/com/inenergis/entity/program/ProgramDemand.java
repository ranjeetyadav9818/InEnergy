package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DemandMinType;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

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
@ToString(exclude = "profile")
@Entity
@Table(name = "PROGRAM_DEMAND")
public class ProgramDemand extends IdentifiableEntity {

    @Column(name = "CALCULATION_INDICATOR")
    private String calculationIndicator;

    @Column(name = "CALCULATION_VALUE")
    private String calculationValue;

    @Column(name = "MIN_VALUE")
    private BigDecimal minValue;

    @Column(name = "MIN_UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit minUnit;

    @Column(name = "DEMAND_MIN_TYPE")
    @Enumerated(EnumType.STRING)
    private DemandMinType demandMinType;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_SEASON_ID")
    private ProgramSeason programSeason;

    @Column(name = "TIME_HORIZON")
    private Integer timeHorizon;

    @Column(name = "PRIOR_MONTHS")
    private Integer priorMonths;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    public boolean notFilledIn() {
        return StringUtils.isEmpty(calculationIndicator) &&
                StringUtils.isEmpty(calculationValue) &&
                demandMinType == null;
    }
}