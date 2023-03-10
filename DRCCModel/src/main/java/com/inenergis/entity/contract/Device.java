package com.inenergis.entity.contract;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.assetTopology.AssetAttribute;
import com.inenergis.entity.maintenanceData.PowerSource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "DEVICE")
@Getter
@Setter
@NoArgsConstructor
public class Device extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "EQUIPMENT_ID")
    private String equipmentId;

    @Column(name = "POWER_SOURCE")
    private String powerSource;

    @ManyToOne
    @JoinColumn(name = "ENTITY_ID")
    ContractEntity entity;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractDevice> contractDevices;


    public Device(ContractEntity entity) {
        super();
        this.entity = entity;
    }


}
