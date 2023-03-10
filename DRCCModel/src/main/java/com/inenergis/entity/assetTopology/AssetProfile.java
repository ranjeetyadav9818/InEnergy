package com.inenergis.entity.assetTopology;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.entity.genericEnum.AssetProfileType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(of = "name")
@Entity
@Table(name = "ASSET_PROFILE")
public class AssetProfile extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "assetProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetProfileAttribute> attributes;

    @ManyToOne
    @JoinColumn(name = "NETWORK_TYPE_ID")
    private NetworkType networkType;

    @Column(name = "ASSET_PROFILE_TYPE")
    @Enumerated(EnumType.STRING)
    private AssetProfileType assetProfileType;

    @OneToMany(mappedBy = "assetProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Asset> assets;

    @OneToMany(mappedBy = "assetProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceLink> deviceLinks;

    public List<AssetProfileAttribute> getDeviceProfileAttributes() {
        return attributes.stream()
                .filter(a -> a instanceof DeviceProfileAttribute)
                .sorted(Comparator.comparing(AssetProfileAttribute::getOrder))
                .collect(Collectors.toList());
    }

    public List<AssetProfileAttribute> getCatalogProfileAttributes() {
        return attributes.stream()
                .filter(a -> a instanceof CatalogProfileAttribute)
                .sorted(Comparator.comparing(AssetProfileAttribute::getOrder))
                .collect(Collectors.toList());
    }

    public List<AssetProfileAttribute> getConnectionProfileAttributes() {
        return attributes.stream()
                .filter(a -> a instanceof ConnectionProfileAttribute)
                .sorted(Comparator.comparing(AssetProfileAttribute::getOrder))
                .collect(Collectors.toList());
    }
}
