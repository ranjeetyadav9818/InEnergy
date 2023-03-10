package com.inenergis.controller.asset;

import com.inenergis.controller.lazyDataModel.LazyAssetDataModel;
import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.genericEnum.AssetProfileType;
import com.inenergis.service.AssetDeviceService;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;
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
public class CatalogController implements Serializable{

    Logger log = LoggerFactory.getLogger(CatalogController.class);

    public static final String ASSET_0_REMOVED = "Asset {0} removed.";
    public static final String ASSET_0_CAN_NOT_BE_REMOVED_IT_HAS_1_DEVICES_LINKED = "Asset {0} Can not be removed. It has {1} devices linked.";
    public static final String ERROR_TRYING_TO_SAVE_THE_ASSET = "Error trying to save the Asset, please try again later and contact your administrator if you keep having this problem";
    @Inject
    protected AssetService assetService;

    @Inject
    protected UIMessage uiMessage;

    @Inject
    private ContractEntityService contractEntityService;

    @Inject
    private ManufacturerService manufacturerService;

    @Inject
    private AssetProfileService assetProfileService;

    @Inject
    private AssetGroupService assetGroupService;

    @Inject
    protected AssetDeviceService assetDeviceService;

    @Inject
    protected NetworkTypeService networkTypeService;

    @Inject
    EntityManager entityManager;

    protected String assetGroup;
    protected String manufacturer;
    protected ContractEntity supplier;
    protected Integer assetLevel;
    protected Long catalogId;
    protected String catalogName;
    protected Asset asset;

    protected List<AssetProfile> assetProfiles;
    protected List<AssetGroup> assetGroups;
    protected List<NetworkType> networkTypes;

    protected List<Manufacturer> manufacturers;
    protected List<ContractEntity> contractEntities;
    protected boolean editMode = false;
    private NetworkType networkType;
    private LazyAssetDataModel lazyAssetDataModel;


    public void search() {
        lazyAssetDataModel = new LazyAssetDataModel(entityManager, generateStatusPrefFilter());
    }

    public void clearFilter() {
        assetGroup = null;
        assetLevel = null;
        catalogId = null;
        catalogName = null;
        manufacturer = null;
        search();
    }

    Map<String, Object> generateStatusPrefFilter() {
        Map<String, Object> preFilter = new HashMap<>();
        if (StringUtils.isNotBlank(assetGroup)) {
            preFilter.put("assetGroup.name", assetGroup);
        }
        if (catalogId != null) {
            preFilter.put("id", catalogId);
        }
        if (assetLevel != null) {
            preFilter.put("assetGroup.level", assetLevel);
        }
        if (StringUtils.isNotBlank(manufacturer)) {
            preFilter.put("manufacturer.name", manufacturer);
        }
        if (StringUtils.isNotBlank(catalogName)) {
            preFilter.put("name", catalogName);
        }
        if (supplier != null) {
            preFilter.put("supplier.id", supplier.getId());
        }
        if (networkType != null) {
            preFilter.put("assetProfile.networkType.id", networkType.getId());
            preFilter.put("assetGroup.commodityType", networkType.getCommodityType());
        }
        return preFilter;
    }


    public void save() {
        try {
            assetService.saveOrUpdate(asset);
            uiMessage.addMessage("Asset {0} created", asset.getName());
            clear();
            Ajax.update("@form");
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_TRYING_TO_SAVE_THE_ASSET);
            log.warn("Error saving a Asset", e);
        }
    }

    public void clear() {
        editMode = false;
    }

    @PostConstruct
    protected void init() {
        networkType = networkTypeService.getById(ParameterEncoderService.getDecodedParameterAsLong("nt"));
        doInit();
    }

    protected void doInit() {
        contractEntities = contractEntityService.getAll();
        manufacturers = manufacturerService.getAll();
        assetProfiles = assetProfileService.getBYNetworkType(networkType.getId(), AssetProfileType.CATALOG_INVENTORY);
        assetGroups = assetGroupService.getAllBYComodityType(networkType.getCommodityType());
        lazyAssetDataModel = new LazyAssetDataModel(entityManager, generateStatusPrefFilter());
    }

    public List<ContractEntity> completeEntity(String query) {
        return contractEntityService.getByBusinessName(query);
    }


    public void loadCatalogAttributes(AssetProfile assetProfile) {
        asset.buildCatalogAttributesFromProfile(assetProfile.getCatalogProfileAttributes());
    }

    public void remove(Asset asset) throws IOException {
        final Long numDevices = assetDeviceService.countByAsset(asset);
        if (numDevices == 0) {
            assetService.delete(asset);
            uiMessage.addMessage(ASSET_0_REMOVED,asset.getName());
        } else {
            uiMessage.addMessage(ASSET_0_CAN_NOT_BE_REMOVED_IT_HAS_1_DEVICES_LINKED, FacesMessage.SEVERITY_ERROR,asset.getId(),numDevices);
        }
    }


    public void add() {
        asset = new Asset();
        editMode = true;
    }

    public void update(Asset asset) {
        this.asset = asset;
        editMode = true;
    }

    public void goToAssetDetails(Asset asset) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("AssetDetails.xhtml?o=" + ParameterEncoderService.encode(asset.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onRowEdit(RowEditEvent event) {
        Asset transmissionAsset = (Asset) event.getObject();
        try {
            asset = assetService.saveOrUpdate(transmissionAsset);
        } catch (IOException e) {
            uiMessage.addMessage(CatalogController.ERROR_TRYING_TO_SAVE_THE_ASSET);
            log.warn("Error saving the asset", e);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        this.asset = (Asset) event.getObject();
    }
}
