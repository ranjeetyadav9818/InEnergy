package com.inenergis.controller.assetTopology;

import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.AssetProfileAttribute;
import com.inenergis.entity.assetTopology.CatalogProfileAttribute;
import com.inenergis.entity.assetTopology.ConnectionProfileAttribute;
import com.inenergis.entity.assetTopology.DeviceProfileAttribute;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AttributeService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class AssetProfileDetailController implements Serializable {


    public static final String THE_ATTRIBUTE_0_CAN_NOT_BE_REMOVED_BECAUSE_IS_IN_USE = "The Attribute {0} can not be removed because is in use";
    @Inject
    private AssetProfileService profileService;

    @Inject
    private AttributeService attributeService;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private NetworkTypeService networkTypeService;

    private AssetProfile profile;
    private Integer activeIndex;
    private CatalogProfileAttribute newCatalogProfileAttribute;
    private DeviceProfileAttribute newDeviceProfileAttribute;
    private ConnectionProfileAttribute newConnectionProfileAttribute;
    private NetworkType networkTypeFilter;
    private List<NetworkType> networkTypes;

    @PostConstruct
    public void init() {
        final Long profileId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        doInit(profileId);
    }

    private void doInit(Long profileId) {
        profile = profileService.getById(profileId);
        newCatalogProfileAttribute = null;
        newDeviceProfileAttribute = null;
        recalculateOrder(profile.getCatalogProfileAttributes());
        recalculateOrder(profile.getDeviceProfileAttributes());
        recalculateOrder(profile.getConnectionProfileAttributes());
        networkTypes = networkTypeService.getAll();

    }

    public void remove(AssetProfileAttribute attribute) {
        profile.getAttributes().remove(attribute);
        profile = profileService.save(profile);
    }

    public void move(AssetProfileAttribute attribute, List attributeList, boolean up) {
        List<AssetProfileAttribute> list = attributeList;
        final int index = list.indexOf(attribute);
        list.remove(attribute);
        final int newIndex = up ? index - 1 : index + 1;
        if (newIndex < 0 || newIndex > list.size()) {
            return;
        }
        list.add(newIndex, attribute);
        recalculateOrder(list);
        profile = profileService.save(profile);
    }

    private void recalculateOrder(List param) {
        List<AssetProfileAttribute> list = param;
        if (CollectionUtils.isNotEmpty(list)) {
            Long i = 1L;
            for (AssetProfileAttribute attr : list) {
                attr.setOrder(i++);
            }
        }
    }

    public void saveProfile(RowEditEvent event) {
        profile = profileService.save(profile);
    }


    public void editNewCatalogProfileAttribute() {
        activeIndex = 0;
        newCatalogProfileAttribute = new CatalogProfileAttribute();
        newCatalogProfileAttribute.setAssetProfile(profile);
        newCatalogProfileAttribute.setOrder(Long.valueOf(profile.getCatalogProfileAttributes().size() + 1));
        profile = profileService.save(profile);
    }

    public void editNewConnectionProfileAttribute() {
        activeIndex = 0;
        newConnectionProfileAttribute = new ConnectionProfileAttribute();
        newConnectionProfileAttribute.setAssetProfile(profile);
        newConnectionProfileAttribute.setOrder(Long.valueOf(profile.getConnectionProfileAttributes().size() + 1));
        profile = profileService.save(profile);
    }

    public void editNewDeviceProfileAttribute() {
        newDeviceProfileAttribute = new DeviceProfileAttribute();
        newDeviceProfileAttribute.setAssetProfile(profile);
        newDeviceProfileAttribute.setOrder(Long.valueOf(profile.getDeviceProfileAttributes().size() + 1));
        profile = profileService.save(profile);
        activeIndex = 1;
    }

    public void addNewCatalogProfileAttribute() {
        profile.getAttributes().add(newCatalogProfileAttribute);
        newCatalogProfileAttribute = null;
        profile = profileService.save(profile);
        activeIndex = 0;
    }

    public void addNewConnectionProfileAttribute() {
        profile.getAttributes().add(newConnectionProfileAttribute);
        newConnectionProfileAttribute = null;
        profile = profileService.save(profile);
        activeIndex = 0;
    }

    public void cancelNewCatalogProfileAttribute() {
        newCatalogProfileAttribute = null;
    }

    public void cancelNewDeviceProfileAttribute() {
        newCatalogProfileAttribute = null;
    }

    public void cancelNewConnectionProfileAttribute() {
        newConnectionProfileAttribute = null;
    }

    public void addNewDeviceProfileAttribute() {
        profile.getAttributes().add(newDeviceProfileAttribute);
        newDeviceProfileAttribute = null;
        profile = profileService.save(profile);
        activeIndex = 1;
    }
}
