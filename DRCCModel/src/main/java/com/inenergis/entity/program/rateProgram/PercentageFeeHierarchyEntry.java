package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.PercentageHierarchyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "PERCENTAGE_FEE_HIERARCHY")
public class PercentageFeeHierarchyEntry extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private PercentageHierarchyType type;

    @Column(name = "AREA")
    private String area;

    @Column(name = "COMMODITY")
    @Enumerated(EnumType.STRING)
    private CommodityType commodity;

    @Column(name = "PERCENTAGE_VALUE")
    private BigDecimal percentage;

}