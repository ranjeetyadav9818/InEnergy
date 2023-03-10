package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString(exclude = "energyContract")
@Entity
@Table(name = "ENERGY_CONTRACT_BILL_MONTH")
public class BillMonth extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_ID")
    private EnergyContract energyContract;

    @Column(name = "MONTH")
    private Integer month;

    public BillMonth() {
    }

    public BillMonth(EnergyContract energyContract) {
        this.energyContract = energyContract;
    }

    public BillMonth(EnergyContract energyContract, Integer month) {
        this.energyContract = energyContract;
        this.month = month;
    }
}