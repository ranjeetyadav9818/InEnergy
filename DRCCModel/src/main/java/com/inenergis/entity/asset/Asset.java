package com.inenergis.entity.asset;

import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.assetTopology.AssetAttribute;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.AssetProfileAttribute;
import com.inenergis.entity.assetTopology.CatalogAttribute;
import com.inenergis.entity.assetTopology.CatalogProfileAttribute;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.genericEnum.AssetOwnership;
import com.inenergis.entity.genericEnum.AssetUsage;
import com.inenergis.entity.program.ProgramProfileAsset;
import com.inenergis.entity.program.RatePlanProfileAsset;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

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
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(of = "name", callSuper = false)
@ToString(of = "name")
@Table(name = "ASSET")
@Entity
public class Asset extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "ACTIVE")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "MANUFACTURER_ID")
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID")
    private ContractEntity supplier;

    @Column(name = "SUPPLIER_PART_NUMBER")
    private String supplierPartNumber;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "EXTERNAL_ID")
    private Long externalId;

    @ManyToOne
    @JoinColumn(name = "ASSET_PROFILE_ID")
    private AssetProfile assetProfile;

    @ManyToOne
    @JoinColumn(name = "ASSET_GROUP_ID")
    private AssetGroup assetGroup;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetAttribute> assetAttributes;

    //BTM asset
    @Column(name = "OWNERSHIP")
    @Enumerated(EnumType.STRING)
    private AssetOwnership ownership;

    @Column(name = "ASSET_USAGE")
    @Enumerated(EnumType.STRING)
    private AssetUsage usage;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramProfileAsset> programProfileAssets;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatePlanProfileAsset> ratePlanProfileAssets;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AssetDevice.class)
    private List<AssetDevice> assetDevices;

    @Transient
    public void buildCatalogAttributesFromProfile(List<AssetProfileAttribute> profileAttributes) {
        if (CollectionUtils.isEmpty(assetAttributes)) {
            assetAttributes = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(profileAttributes)) {
            assetAttributes.addAll(profileAttributes.stream().filter(a -> a.getClass().isAssignableFrom(CatalogProfileAttribute.class)).map(a -> new CatalogAttribute(a, this)).collect(Collectors.toList()));
        }
    }

    public List<CatalogAttribute> getCatalogAttributes(){
        if (CollectionUtils.isEmpty(assetAttributes)) {
            return new ArrayList<>();
        }
        return assetAttributes.stream().filter(a -> a instanceof CatalogAttribute).map(a -> (CatalogAttribute) a).collect(Collectors.toList());
    }

}