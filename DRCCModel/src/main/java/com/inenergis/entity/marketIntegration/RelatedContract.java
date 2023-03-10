package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.RelatedContractType;
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

@Getter
@Setter
@ToString(exclude = "energyContract")
@Entity
@Table(name = "ENERGY_CONTRACT_RELATED_CONTRACTS")
public class RelatedContract extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_ID")
    private EnergyContract energyContract;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private RelatedContractType type;

    @ManyToOne
    @JoinColumn(name = "RELATED_ENERGY_CONTRACT_ID")
    private EnergyContract relatedEnergyContract;

    public RelatedContract() {
    }

    public RelatedContract(EnergyContract energyContract) {
        this.energyContract = energyContract;
    }
}