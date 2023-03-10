package com.inenergis.entity.device;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by egamas on 22/11/2017.
 */
@Entity
@Table(name = "DEVICE_LINK_TARGET")
@Getter
@Setter
public class DeviceLinkTarget extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "DEVICE_LINK_ID")
    private DeviceLink deviceLink;

    @ManyToOne
    @JoinColumn(name = "DEVICE_TARGET_ID")
    private AssetDevice assetDevice;

}
