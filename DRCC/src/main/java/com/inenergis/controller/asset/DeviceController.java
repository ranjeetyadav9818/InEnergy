package com.inenergis.controller.asset;

import com.inenergis.controller.lazyDataModel.device.LazyDeviceDataModel;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.DeviceService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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

@Getter
@Setter
@Named
@ViewScoped
public class DeviceController implements Serializable{


    public static final String ERROR_TRYING_TO_DELETE_THE_DEVICE = "Error trying to delete the Device, please try again later and contact your administrator if you keep having this problem";
    public static final String ERROR_TRYING_TO_UPDATE_THE_DEVICE = "Error trying to update the Device, please try again later and contact your administrator if you keep having this problem";
    public static final String ERROR_TRYING_TO_SAVE_THE_DEVICE = "Error trying to save the Device, please try again later and contact your administrator if you keep having this problem";

    Logger log = LoggerFactory.getLogger(DeviceController.class);

    @Inject
    private ManufacturerService manufacturerService;
    @Inject
    private ContractEntityService contractEntityService;
    @Inject
    private NetworkTypeService netWorkTypeService;
    @Inject
    AssetService assetService;

    @Inject
    EntityManager entityManager;
    @Inject
    UIMessage uiMessage;

    @Inject
    protected DeviceService deviceService;

    protected AssetDevice device;
    protected List<Asset> assets;

    protected String assetGroup;
    protected String manufacturer;
    protected ContractEntity supplier;
    protected Integer assetLevel;
    protected Long deviceId;
    protected String deviceName;

    protected List<Manufacturer> manufacturers;
    protected boolean editMode = false;
    private NetworkType networkType;

    private LazyDeviceDataModel lazyDeviceDataModel;

    public void clearFilter() {
        assetGroup = null;
        assetLevel = null;
        deviceId = null;
        deviceName = null;
        manufacturer = null;
        search();
    }

    Map<String, Object> generateStatusPrefFilter() {
        Map<String, Object> preFilter = new HashMap<>();
        if (deviceId != null) {
            preFilter.put("id", deviceId);
        }
        if (StringUtils.isNotBlank(deviceName)) {
            preFilter.put("name", deviceName);
        }
        if (StringUtils.isNotBlank(assetGroup)) {
            preFilter.put("asset.assetGroup.name", assetGroup);
        }
        if (StringUtils.isNotBlank(manufacturer)) {
            preFilter.put("asset.manufacturer.name", manufacturer);
        }
        if (assetLevel != null) {
            preFilter.put("asset.assetGroup.level", assetLevel);
        }
        if (supplier != null) {
            preFilter.put("asset.supplier.id", supplier.getId());
        }
        if (networkType != null) {
            preFilter.put("asset.assetProfile.networkType.id", networkType.getId());
        }

        return preFilter;
    }

    public void clear() {
        editMode = false;
    }

    public void save() {
        if (device == null || device.getAsset() == null || device.getAsset().getAssetProfile() == null) {
            return;
        }
        final Asset asset = device.getAsset();
        if (CollectionUtils.isEmpty(device.getAssetAttributes())) {
            device.buildInventoryAttributesFromProfile(asset.getAssetProfile().getAttributes());
        }
        try {
            deviceService.saveOrUpdate(device);
            uiMessage.addMessage("Device {0} created.", device.getName());
            Ajax.update("@form");
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_TRYING_TO_SAVE_THE_DEVICE);
            log.warn("Error saving the asset", e);
        }
        clear();
    }
    @PostConstruct
    protected void init() {
        manufacturers = manufacturerService.getAll();
        networkType = netWorkTypeService.getById(ParameterEncoderService.getDecodedParameterAsLong("nt"));
        lazyDeviceDataModel = new LazyDeviceDataModel(entityManager, generateStatusPrefFilter());
        assets = assetService.getAllByNetworkType(networkType.getId());
    }

    public List<ContractEntity> completeEntity(String query) {
        return contractEntityService.getByBusinessName(query);
    }

    public void loadDeviceAttributes(AssetDevice device) {
        device.buildInventoryAttributesFromProfile(device.getAsset().getAssetProfile().getAttributes());
    }

    public void remove(AssetDevice device) {
        try {
            deviceService.delete(device);
            uiMessage.addMessage("Device {0} deleted.", device.getName());
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_TRYING_TO_DELETE_THE_DEVICE);
            log.warn("Error saving the asset", e);
        }
    }
    public void search() {
        lazyDeviceDataModel = new LazyDeviceDataModel(entityManager, generateStatusPrefFilter());
    }

    public void add() {
        device = new AssetDevice();
        editMode = true;
    }

    public void update(AssetDevice distributionDevice) {
        device = distributionDevice;
        editMode = true;
    }

    public void goToDeviceDetails(AssetDevice distributionDevice) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("DeviceDetails.xhtml?o=" + ParameterEncoderService.encode(distributionDevice.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onRowEdit(RowEditEvent event) {
        AssetDevice distributionDevice = (AssetDevice) event.getObject();
        try {
            deviceService.saveOrUpdate(distributionDevice);
        } catch (IOException e) {
            uiMessage.addMessage(DeviceController.ERROR_TRYING_TO_SAVE_THE_DEVICE);
            log.warn("Error saving the asset", e);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        this.device = (AssetDevice) event.getObject();
    }
}
