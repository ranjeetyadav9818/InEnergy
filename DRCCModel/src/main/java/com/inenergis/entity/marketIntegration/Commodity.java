package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CommodityPowerSource;
import com.inenergis.entity.genericEnum.CommodityProductType;
import com.inenergis.entity.genericEnum.CommodityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
import java.util.List;

@Getter
@Setter

@ToString(exclude = "energyContract")
@Entity
@Table(name = "ENERGY_CONTRACT_COMMODITY")
public class Commodity extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_ID")
    private EnergyContract energyContract;

    @Column(name = "COMMODITY_TYPE")
    @Enumerated(EnumType.STRING)
    private CommodityType commodityType;

    @Column(name = "PRODUCT_TYPE")
    @Enumerated(EnumType.STRING)
    private CommodityProductType commodityProductType;

    @ManyToOne
    @JoinColumn(name = "COMMODITY_PROGRAM_ID")
    private CommodityProgram commodityProgram;

    @ManyToOne
    @JoinColumn(name = "COMMODITY_SUB_PROGRAM_ID")
    private CommoditySubProgram commoditySubProgram;

    @Column(name = "COMMODITY_POWER_SOURCE")
    @Enumerated(EnumType.STRING)
    private CommodityPowerSource commodityPowerSource;

    @OneToMany(mappedBy = "commodity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quantity> quantities;

    public Commodity() {
    }

    public Commodity(EnergyContract energyContract) {
        this.energyContract = energyContract;
    }
}