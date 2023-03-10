package com.inenergis.entity.device;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetAttribute;
import com.inenergis.entity.assetTopology.AssetProfileAttribute;
import com.inenergis.entity.assetTopology.DeviceAttribute;
import com.inenergis.entity.assetTopology.DeviceProfileAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Table(name = "ASSET_DEVICE")
@Entity
public class AssetDevice extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ASSET_ID")
    private Asset asset;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXTERNAL_ID")
    private Long externalId;

    @ManyToMany(mappedBy = "sources", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DeviceLink> sourceLinks;

    @ManyToMany(mappedBy = "targets", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DeviceLink> targetLinks;

    public List<DeviceLink> getLinks() {
        List<DeviceLink> links = new ArrayList<>(sourceLinks);
        links.addAll(targetLinks);
        return links;
    }

    @Column(name = "ADDRESS_1")
    private String address1;

    @Column(name = "ADDRESS_2")
    private String address2;

    @Column(name = "ADDRESS_3")
    private String address3;

    @Column(name = "CITY")
    private String city;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "LATITUDE")
    private BigDecimal latitude;

    @Column(name = "LONGITUDE")
    private BigDecimal longitude;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceParty> deviceParties;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetAttribute> assetAttributes;

    public List<DeviceAttribute> getDeviceAttributes() {
        if (CollectionUtils.isNotEmpty(assetAttributes)) {
            return assetAttributes.stream().filter(a -> a instanceof DeviceAttribute).map(a -> (DeviceAttribute) a).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void buildInventoryAttributesFromProfile(List<AssetProfileAttribute> profileAttributes) {
         assetAttributes = profileAttributes.stream().filter(a -> a instanceof DeviceProfileAttribute)
                .map(a -> new DeviceAttribute(a, this))
                .collect(Collectors.toList());
    }

}