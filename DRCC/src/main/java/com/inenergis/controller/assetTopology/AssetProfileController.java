package com.inenergis.controller.assetTopology;

import com.inenergis.controller.lazyDataModel.LazyAssetProfileDataModel;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.AssetProfileType;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AttributeService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class AssetProfileController implements Serializable {

    public static final String THE_ASSET_TYPE_PROFILE_0_CAN_NOT_BE_DELETED_BECAUSE_IT_HAS_ASSETS = "The Asset Type Profile {0} can not be deleted because it has been used by assets.";
    public static final String THE_ASSET_TYPE_PROFILE_0_CAN_NOT_BE_DELETED_BECAUSE_IT_HAS_ATTRIBUTES = "The Asset Type Profile {0} can not be deleted because it has attributes.";
    @Inject
    private UIMessage uiMessage;
    @Inject
    private EntityManager entityManager;
    @Inject
    private AssetProfileService assetProfileService;
    @Inject
    private AttributeService attributeService;
    @Inject
    private NetworkTypeService networkTypeService;

    private Logger log = LoggerFactory.getLogger(AssetProfileController.class);

    private LazyAssetProfileDataModel lazyAssetProfileDataModel;
    private Map<String, Object> permanentFilters = new HashMap();
    private NetworkType networkTypeFilter;
    private String assetProfileName;
    private AssetProfile newProfile;
    private AssetProfile profileToDelete;
    private List<NetworkType> networkTypes;
    private CommodityType commodityType;
    private AssetProfileType profileTypeFilter;

    @PostConstruct
    public void init() {
        commodityType = ParameterEncoderService.getCommodityTypeParameter();
        doInit();
    }

    private void doInit() {
        networkTypes = networkTypeService.getAllBy(commodityType);
        search();
    }

    public void generateFilters() {
        permanentFilters = new HashMap();
        if (networkTypeFilter != null) {
            permanentFilters.put("networkType.id", networkTypeFilter.getId());
        }
        if (assetProfileName != null) {
            permanentFilters.put("name", assetProfileName);
        }
        if (commodityType != null) {
            permanentFilters.put("networkType.commodityType", commodityType);
        }
        if (profileTypeFilter != null) {
            permanentFilters.put("assetProfileType", profileTypeFilter);
        }
    }

    public void search() {
        generateFilters();
        lazyAssetProfileDataModel = new LazyAssetProfileDataModel(entityManager, permanentFilters);
    }

    public void remove(AssetProfile profile) {
        profileToDelete = profile;
    }

    public void confirmRemove() {
        if (CollectionUtils.isNotEmpty(profileToDelete.getAssets())) {
            uiMessage.addMessage(THE_ASSET_TYPE_PROFILE_0_CAN_NOT_BE_DELETED_BECAUSE_IT_HAS_ASSETS, FacesMessage.SEVERITY_ERROR, profileToDelete.getName());
            profileToDelete = null;
            return;
        }
        assetProfileService.delete(profileToDelete);
        profileToDelete = null;
    }

    public void addNew() {
        newProfile = new AssetProfile();
    }

    public void saveNew() {
        newProfile = assetProfileService.save(newProfile);
        networkTypeFilter = newProfile.getNetworkType();
        assetProfileName = newProfile.getName();
        newProfile = null;
    }

    public void cancelNew() {
        newProfile = null;
    }

    public void cancelRemove() {
        profileToDelete = null;
    }

    public void onSelectAssetProfile(SelectEvent event) throws IOException {
        AssetProfile assetProfile = (AssetProfile) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext().redirect("AssetProfileDetail.xhtml?o=" + ParameterEncoderService.encode(assetProfile.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

}
