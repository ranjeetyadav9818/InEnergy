package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.genericEnum.PartyRole;
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
@Table(name = "ENERGY_CONTRACT_PARTY")
public class Party extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_ID")
    private EnergyContract energyContract;

    @ManyToOne
    @JoinColumn(name = "ENTITY_ID")
    private ContractEntity entity;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private PartyRole role;

    public Party() {
    }

    public Party(EnergyContract energyContract) {
        this.energyContract = energyContract;
    }
}