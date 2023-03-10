package com.inenergis.entity.device;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.contract.ContractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ASSET_DEVICE_PARTY")
@Getter
@Setter
public class DeviceParty extends IdentifiableEntity {
    @Column(name = "TYPE")
    String type;

    @ManyToOne
    @JoinColumn(name = "DEVICE_ID")
    AssetDevice device;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_ENTITY_ID")
    ContractEntity contractEntity;

    public DeviceParty() {
    }

    public DeviceParty(AssetDevice device) {
        this.device = device;
    }
}