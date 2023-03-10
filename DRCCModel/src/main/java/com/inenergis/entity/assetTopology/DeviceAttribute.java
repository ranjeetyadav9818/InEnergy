package com.inenergis.entity.assetTopology;

import com.inenergis.entity.device.AssetDevice;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DEVICE_ATTR")
@NoArgsConstructor
public class DeviceAttribute extends AssetAttribute {

    public DeviceAttribute(AssetProfileAttribute profileAttribute, AssetDevice device) {
        super.setDevice(device);
        this.profileAttribute = profileAttribute;
        this.name = profileAttribute.getName();
        this.mandatory = profileAttribute.getMandatory();
        this.order = profileAttribute.getOrder();
        this.attributeValidation = profileAttribute.getAttributeValidation();
    }
}
