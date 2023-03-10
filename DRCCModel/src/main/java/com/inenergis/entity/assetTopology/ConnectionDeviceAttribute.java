package com.inenergis.entity.assetTopology;

import com.inenergis.entity.device.DeviceLink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("CONN_DEVICE_ATTR")
@NoArgsConstructor
@Getter
@Setter
public class ConnectionDeviceAttribute extends AssetAttribute {

    @ManyToOne
    @JoinColumn(name = "DEVICE_LINK_ID")
    private DeviceLink deviceLink;

    public ConnectionDeviceAttribute(AssetProfileAttribute profileAttribute, DeviceLink deviceLink) {
        this.setDeviceLink(deviceLink);
        this.profileAttribute = profileAttribute;
        this.name = profileAttribute.getName();
        this.mandatory = profileAttribute.getMandatory();
        this.order = profileAttribute.getOrder();
        this.attributeValidation = profileAttribute.getAttributeValidation();
    }
}
