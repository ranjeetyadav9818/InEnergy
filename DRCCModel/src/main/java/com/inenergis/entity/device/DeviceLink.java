package com.inenergis.entity.device;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.AssetProfileAttribute;
import com.inenergis.entity.assetTopology.ConnectionDeviceAttribute;
import com.inenergis.entity.assetTopology.ConnectionProfileAttribute;
import com.inenergis.entity.genericEnum.DeviceLinkType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.common.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "DEVICE_LINK")
@Getter
@Setter
@EqualsAndHashCode(of = {"name", "type", "externalId"}, callSuper = false)
public class DeviceLink extends IdentifiableEntity {

    public DeviceLink() {
        sources = new ArrayList<>();
        targets = new ArrayList<>();
        linkAttributes = new ArrayList<>();
    }

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private DeviceLinkType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "DEVICE_LINK_SOURCE", joinColumns = {@JoinColumn(name = "DEVICE_LINK_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "DEVICE_SOURCE_ID", nullable = false)})
    private List<AssetDevice> sources;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "DEVICE_LINK_TARGET", joinColumns = {@JoinColumn(name = "DEVICE_LINK_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "DEVICE_TARGET_ID", nullable = false)})
    private List<AssetDevice> targets;

    @Column(name = "EXTERNAL_ID")
    private Long externalId;

    @ManyToOne
    @JoinColumn(name = "ASSET_PROFILE_ID")
    private AssetProfile assetProfile;

    @OneToMany(mappedBy = "deviceLink", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConnectionDeviceAttribute> linkAttributes;

    public void buildConnectionAttributesFromProfile(List<AssetProfileAttribute> profileAttributes) {
        if (CollectionUtils.isEmpty(linkAttributes)) {
            linkAttributes = new ArrayList<>();
        }
        linkAttributes = profileAttributes.stream().filter(a -> a.getClass().isAssignableFrom(ConnectionProfileAttribute.class))
                .map(a -> new ConnectionDeviceAttribute(a, this))
                .collect(Collectors.toList());
    }

    public List<AssetDevice> getDevices() {
        final ArrayList<AssetDevice> assetDevices = new ArrayList<>(sources);
        assetDevices.addAll(targets);
        return assetDevices;
    }

    public String commaSeparatedSources() {
        if (getSources() == null) {
            return null;
        }
        return generateCommaSeparatedDevices(getSources());
    }

    public String commaSeparatedTargets() {
        if (getTargets() == null) {
            return null;
        }
        return generateCommaSeparatedDevices(getTargets());
    }

    private String generateCommaSeparatedDevices(List<AssetDevice> devices) {
        StringBuilder sb = new StringBuilder();
        Iterator<AssetDevice> iterator = devices.iterator();
        while (iterator.hasNext()){
            sb.append(iterator.next().getName());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String commaSeparatedAttributes() {
        if (getLinkAttributes() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<ConnectionDeviceAttribute> iterator = getLinkAttributes().iterator();
        while (iterator.hasNext()){
            ConnectionDeviceAttribute attribute = iterator.next();
            sb.append(attribute.getName()).append("=").append(attribute.printValue());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}