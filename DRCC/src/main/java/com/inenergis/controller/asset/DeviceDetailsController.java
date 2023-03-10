package com.inenergis.controller.asset;

import com.inenergis.controller.assetTopology.ElementData;
import com.inenergis.controller.carousel.AssetCarousel;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.Note;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.ConnectionDeviceAttribute;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.entity.device.DeviceParty;
import com.inenergis.entity.genericEnum.AssetProfileType;
import com.inenergis.entity.maintenanceData.PartyType;
import com.inenergis.service.AssetDeviceService;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.DeviceLinkService;
import com.inenergis.service.MaintenanceDataService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.picketlink.Identity;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StateMachineConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
import org.primefaces.model.diagram.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class DeviceDetailsController implements Serializable{


    public static final String ERROR_TRYING_TO_SAVE_THE_PARTY_DEVICE = "Error trying to save the Party Device, please try again later and contact your administrator if you keep having this problem";
    public static final String ERROR_TRYING_TO_SAVE_A_LINK = "Error trying to save a Link , please try again later and contact your administrator if you keep having this problem";
    Logger log = LoggerFactory.getLogger(DeviceDetailsController.class);

    @Inject
    protected AssetDeviceService deviceService;

    @Inject
    protected AssetService assetService;

    @Inject
    private ManufacturerService manufacturerService;

    @Inject
    private MaintenanceDataService maintenanceDataService;

    @Inject
    private AssetProfileService assetProfileService;

    @Inject
    private AssetGroupService assetGroupService;

    @Inject
    private AssetCarousel assetCarousel;

    @Inject
    private Identity identity;

    @Inject
    private ContractEntityService contractEntityService;

    @Inject
    private DeviceLinkService deviceLinkService;

    @Inject
    private AssetDeviceService assetDeviceService;

    @Inject
    private NetworkTypeService netWorkTypeService;

    @Inject
    private UIMessage uiMessage;

    protected AssetDevice device;
    protected List<? extends Asset> assets;

    protected List<EnergyArrayDataBeanList> entityDetails;
    protected List<Note> notes;
    protected List<AssetProfile> assetProfiles;
    protected List<AssetProfile> linkAssetProfile;
    protected List<AssetGroup> assetGroups;

    protected boolean editMode = false;
    protected boolean editModeTab = false;
    protected boolean partiesEditModeTab = false;
    protected List<Manufacturer> manufacturers;
    protected DeviceParty newDeviceParty;

    protected DeviceLink link = null;

    protected int tabIndex;
    private AssetDevice nullDevice;
    Element mainDevice;
    int position;

    @PostConstruct
    protected void init() {
        device = deviceService.getById(ParameterEncoderService.getDefaultDecodedParameterAsLong());
        entityDetails = new ArrayList<>();
        manufacturers = manufacturerService.getAll();
        assetProfiles = assetProfileService.getAll();
        linkAssetProfile = assetProfileService.getProfilesByCommodityAndType(device.getAsset().getAssetProfile().getNetworkType().getCommodityType(), AssetProfileType.CONNECTION);
        assetGroups = assetGroupService.getAll();
        assets = assetService.getAllByNetworkType(device.getAsset().getAssetProfile().getNetworkType().getId());
        initDiagram();
        String tab = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        if (StringUtils.isNotEmpty(tab)) {
            tabIndex = Integer.parseInt(tab);
        }
        //assetCarousel.generate(entityDetails, device);
        nullDevice = new AssetDevice() {
            @Override
            public Asset getAsset() {
                return null;
            }

            @Override
            public void setAsset(Asset asset) {

            }
        };
    }

    public void save() {
        try {
            device = deviceService.saveOrUpdate(device);
            editMode = false;
            editModeTab = false;
            entityDetails.clear();
            //assetCarousel.generate(entityDetails, device);
            uiMessage.addMessage("Device {0} saved.", device.getName());
            Ajax.update("@form");
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save the Contract Entity, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a Contract Entity", e);
        }
    }

    public void saveParty() {
        device.getDeviceParties().add(newDeviceParty);
        save();
        partiesEditModeTab = false;
    }

    public void removeParty(DeviceParty party) {
        device.getDeviceParties().remove(party);
        save();
    }

    public void editTab() {
        editModeTab = true;
    }

    public void addParty() {
        newDeviceParty = new DeviceParty(device);
        partiesEditModeTab = true;
        tabIndex = 0;
    }

    public void clear() {
        editMode = false;
        editModeTab = false;
    }

    public List<ContractEntity> completeEntity(String query) {
        return contractEntityService.getByBusinessName(query);
    }

    public List<PartyType> getPartyTypes() {
        return maintenanceDataService.getPartyTypes();
    }

    public void editDevice() {
        editMode = true;
    }

    public void onPartiesRowEdit(RowEditEvent event) {
        DeviceParty deviceParty = (DeviceParty) event.getObject();
        device.getDeviceParties().remove(deviceParty);
        device.getDeviceParties().add(deviceParty);

        try {
            deviceService.saveOrUpdate(device);
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_TRYING_TO_SAVE_THE_PARTY_DEVICE);
            log.warn("Error saving a Party Device", e);
        }
    }

    public void addLink() {
        link = new DeviceLink();
        link.getSources().add(device);
    }

    public void addSource() {
        link.getSources().add(nullDevice);
    }

    public void removeSource(AssetDevice source) {
        link.getSources().remove(source);
    }

    public void addTarget() {
        link.getTargets().add(nullDevice);
    }

    public void removeTarget(AssetDevice target) {
        link.getTargets().remove(target);
    }


    public void saveLink() {
        if (CollectionUtils.isEmpty(link.getSources()) || CollectionUtils.isEmpty(link.getTargets())) {
            uiMessage.addMessage("At least one source and one target must be provided", FacesMessage.SEVERITY_ERROR);
            return;
        }
        deviceLinkService.saveOrUpdate(link);
        device = assetDeviceService.getById(device.getId());
        initDiagram();
        cancelLink();
    }

    public void cancelLink() {
        link = null;
    }

    public void removeLink(DeviceLink deviceLink) {
        deviceLinkService.remove(deviceLink);
        device = assetDeviceService.getById(device.getId());
        initDiagram();
        cancelLink();
    }

    protected DefaultDiagramModel model;

    protected AssetDevice selectedItem = null;

    public void initDiagram() {
        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);

        StateMachineConnector connector = new StateMachineConnector();
        connector.setOrientation(StateMachineConnector.Orientation.ANTICLOCKWISE);
        connector.setPaintStyle("{strokeStyle:'#7D7463',lineWidth:3}");
        model.setDefaultConnector(connector);

        //Place elements for all devices with main one at center
        final List<AssetDevice> devicesToConnect = device.getLinks().stream().flatMap(l -> l.getDevices().stream()).distinct().collect(Collectors.toList());
        final int numDevices = devicesToConnect.size();
        if (numDevices == 0) {
            mainDevice = buildElement(device.getName(), device.getUuid(), 0, numDevices);
        } else {
            devicesToConnect.remove(device);
            devicesToConnect.add(numDevices / 2, device);

            for (int i = 0; i < numDevices; i++) {
                final AssetDevice assetDevice = devicesToConnect.get(i);
                Element element = buildElement(assetDevice.getName(), assetDevice.getUuid(), i, numDevices);
                if (i == numDevices / 2) {
                    mainDevice = element;
                }
            }

            for (DeviceLink deviceLink : device.getLinks()) {
                for (AssetDevice source : deviceLink.getSources()) {
                    connectAssets(model.findElement(source.getUuid()), deviceLink.getTargets(), deviceLink);
                }
            }
        }
        mainDevice.setStyleClass("diagram-main-rectangle");
    }

    private void connectAssets(Element originDevice, List<AssetDevice> devices, DeviceLink deviceLink) {
        for (AssetDevice assetDevice : devices) {
            if (assetDevice.getUuid().equals(originDevice.getId())) {
                continue;
            }
            makeConnections(originDevice, deviceLink, assetDevice);
        }
    }

    private void makeConnections(Element mainDevice, DeviceLink deviceLink, AssetDevice assetDevice) {
        Element element = model.findElement(assetDevice.getUuid());
        final EndPoint elementEndPoint = getEndPointByAnchor(mainDevice, ((ElementData) mainDevice.getData()).translateEndpoint((ElementData) element.getData()));
        final EndPoint mainEndPoint = getEndPointByAnchor(element, ((ElementData) element.getData()).translateEndpoint((ElementData) mainDevice.getData()));
        switch (deviceLink.getType()) {
            case OUT: {
                createConnection(model, mainEndPoint, elementEndPoint, deviceLink.getName());
            }
            break;
            case IN: {
                createConnection(model, elementEndPoint, mainEndPoint, deviceLink.getName());
            }
            break;
            case BIDIRECTIONAL: {
                createConnection(model, mainEndPoint, elementEndPoint, deviceLink.getName());
                createConnection(model, elementEndPoint, mainEndPoint, null);
            }
            break;
        }

    }

    private Element buildElement(String name, String uuid, int position, int numElements) {
        ElementData data = ElementData.builder()
                .label(name)
                .position(position)
                .build();
        data.placeElementByPosition(numElements);
        Element element;
        element = new Element(data, data.getX(), data.getY());
        element.setId(uuid);
        addEndPoints(element);
        model.addElement(element);
        return element;
    }


    public void addEndPoints(Element elem) {
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP_LEFT));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP_RIGHT));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.RIGHT));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM_RIGHT));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM_LEFT));
        elem.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
    }

    public static EndPoint getEndPointByAnchor(Element elem, EndPointAnchor anchor) {
        if (CollectionUtils.isEmpty(elem.getEndPoints())) {
            return null;
        }
        final Optional<EndPoint> first = elem.getEndPoints().stream().filter(ep -> ep.getAnchor().equals(anchor)).findFirst();
        if (!first.isPresent()) {
            throw new IllegalArgumentException("Anchor is not present: Element " + elem + " anchor " + anchor);
        }
        return first.get();
    }

    private void createConnection(DefaultDiagramModel model, EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        if (!model.getConnections().contains(conn)) {
            ArrowOverlay arrowOverlay = new ArrowOverlay(20, 20, 1, 1);
            conn.getOverlays().add(arrowOverlay);
            if (label != null) {
                conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
            }
            model.connect(conn);
        } else { //If connection exists update label
            final List<Overlay> overlays = model.getConnections().get(model.getConnections().indexOf(conn)).getOverlays();
            if (CollectionUtils.isNotEmpty(overlays)) {
                final Optional<Overlay> overlay = overlays.stream().filter(o -> o instanceof LabelOverlay).findFirst();
                if (overlay.isPresent()) {
                    LabelOverlay previousLabel = ((LabelOverlay) overlay.get());
                    if (previousLabel.getLabel().indexOf(label) == -1) {
                        previousLabel.setLabel(label + "," + previousLabel.getLabel());
                    }
                }
            }
        }
    }

    public void onElementClicked() {
        String elementId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("elementId");
        final String uuid = elementId.substring(elementId.indexOf("diagram-") + "diagram-".length());
        if (device.getUuid().equals(uuid)) {
            return;
        }
        final List<AssetDevice> allDevices = device.getLinks().stream().flatMap(l -> l.getDevices().stream()).collect(Collectors.toList());
        final Optional<AssetDevice> first = allDevices.stream().filter(d -> d.getUuid().equals(uuid)).findFirst();
        if (first.isPresent()) {
            selectedItem = first.get();
        }
    }

    public void forwardToSelectedItem(AssetDevice device) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("DeviceDetails.xhtml?o=" + ParameterEncoderService.encode(device.getId()) + "&tab=2");
        FacesContext.getCurrentInstance().responseComplete();
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void editAttribute(RowEditEvent event) {
        if (event.getObject() instanceof ConnectionDeviceAttribute) {
            ConnectionDeviceAttribute attribute = (ConnectionDeviceAttribute) event.getObject();
            try {
                deviceLinkService.saveOrUpdate(attribute.getDeviceLink());
            } catch (Exception e) {
                uiMessage.addMessage(ERROR_TRYING_TO_SAVE_A_LINK);
                log.warn("Error saving a Link", e);
            }
        } else {
            try {
                this.device = assetDeviceService.saveOrUpdate(this.device);
            } catch (IOException e) {
                uiMessage.addMessage(ERROR_TRYING_TO_SAVE_THE_PARTY_DEVICE);
                log.warn("Error saving an Asset", e);
            }
        }
    }

    public void loadDeviceAttributes(AssetDevice device) {
        device.buildInventoryAttributesFromProfile(device.getAsset().getAssetProfile().getAttributes());
    }

    public List<AssetDevice> completeAssetDevice(String query) {
        return assetDeviceService.getByName(query);
    }


}