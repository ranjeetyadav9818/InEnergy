package com.inenergis.controller.general;

import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.CatalogAttribute;
import com.inenergis.entity.assetTopology.ConnectionDeviceAttribute;
import com.inenergis.entity.assetTopology.DeviceAttribute;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.entity.genericEnum.AssetOwnership;
import com.inenergis.entity.genericEnum.AssetUsage;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.DeviceLinkType;
import com.inenergis.service.AssetDeviceService;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.DeviceLinkService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.asset.AssetSerializer;
import com.inenergis.util.asset.DeviceSerializer;
import com.inenergis.util.asset.LinkSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class AssetHelper implements Serializable {

    public static final String SEPARATOR = ",";
    public static final String SEPARATOR_REGEXP = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String DOUBLE_QUOTE = "\"";

    @Inject
    private ManufacturerService manufacturerService;

    @Inject
    private ContractEntityService contractEntityService;

    @Inject
    private AssetProfileService assetProfileService;

    @Inject
    private AssetGroupService assetGroupService;

    @Inject
    private AssetService assetService;

    @Inject
    private AssetDeviceService assetDeviceService;

    @Inject
    private DeviceLinkService deviceLinkService;

    @Inject
    private AssetSerializer assetSerializer;

    @Inject
    private DeviceSerializer deviceSerializer;

    @Inject
    private LinkSerializer linkSerializer;

    @Inject
    private NetworkTypeService networkTypeService;

    @Inject
    PropertyAccessor propertyAccessor;


    /*
    0 - Type
    === Common attributes
    1 - External Id
    2 - Name
    3 - Description
    4 - Make
    5 - Active
    6 - Manufacturer Id
    7 - Contract Entity Id
    8 - Supplier Part Number
    9 - Model
    10 -Asset Profile Id
    11- Asset Group Id
    === Behind The Meter attributes
    12- Ownership (enum)
    13- Asset Usage (enum)
    === Distribution attributes
    === Transmission attributes
    */
    public Asset createAsset(String line) throws IOException {
        List<String> values = Arrays.asList(line.split(SEPARATOR_REGEXP));
        Asset asset = new Asset();
        String commodity = values.get(0);
        String networkTypeName = values.get(1);
        try {
            networkTypeService.getBy(CommodityType.valueOf(commodity), networkTypeName);
        } catch (IllegalArgumentException e) {
            throw new IOException("Wrong Network: " + commodity + "," + networkTypeName);
        }
        asset.setExternalId(Long.parseLong(values.get(2)));
        asset.setName(unquoteString(values.get(3)));
        asset.setDescription(unquoteString(values.get(4)));
        asset.setMake(unquoteString(values.get(5)));
        asset.setActive(values.get(6).equals("1"));

        Manufacturer manufacturer = manufacturerService.getById(Long.parseLong(values.get(7)));
        if (manufacturer == null) {
            throw new IOException("Wrong Manufacturer ID: " + values.get(7));
        }
        asset.setManufacturer(manufacturer);

        final String supplier = values.get(8);
        if (StringUtils.isNotEmpty(supplier)) {
            ContractEntity contractEntity = contractEntityService.getById(Long.parseLong(supplier));
            if (contractEntity == null) {
                throw new IOException("Wrong Supplier ID: " + supplier);
            }
            asset.setSupplier(contractEntity);
        }

        asset.setSupplierPartNumber(values.get(9));
        asset.setModel(values.get(10));

        AssetProfile assetProfile = assetProfileService.getById(Long.parseLong(values.get(11)));
        if (assetProfile == null) {
            throw new IOException("Wrong Asset Profile ID: " + values.get(11));
        }
        asset.setAssetProfile(assetProfile);

        AssetGroup assetGroup = assetGroupService.getById(Long.parseLong(values.get(12)));
        if (assetGroup == null) {
            throw new IOException("Wrong Asset Group ID: " + values.get(12));
        }
        asset.setAssetGroup(assetGroup);

        final String ownership = values.get(13);
        if (StringUtils.isNotEmpty(ownership)) {
            try {
                AssetOwnership assetOwnership = AssetOwnership.valueOf(ownership);
                asset.setOwnership(assetOwnership);
            } catch (IllegalArgumentException e) {
                throw new IOException("Wrong Ownership: " + ownership);
            }
        }

        final String usage = values.get(14);
        if (StringUtils.isNotEmpty(usage)) {
            try {
                AssetUsage assetUsage = AssetUsage.valueOf(usage);
                asset.setUsage(assetUsage);
            } catch (IllegalArgumentException e) {
                throw new IOException("Wrong Usage: " + usage);
            }
        }

        int i = 15;
        //import catalog attributes
        asset.buildCatalogAttributesFromProfile(assetProfile.getCatalogProfileAttributes());
        for (CatalogAttribute attribute : asset.getCatalogAttributes()) {
            if (values.size() <= i) {
                if (attribute.getMandatory()) {
                    throw new IOException("Wrong attributes count for the record with external id: " + asset.getExternalId());
                }
            } else {
                try {
                    attribute.setAttribute(values.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    throw new IOException("Wrong attributes count for the record with external id: " + asset.getExternalId());
                }
            }
        }
        return asset;
    }

    public ByteArrayInputStream downloadAssets(CommodityType commodityType) throws IOException, SQLException {
        return assetSerializer.dataToByteArrayInputStream(assetService.getAllByCommodity(commodityType));
    }

    /*
    0 - Type
    === Common attributes
    1 - External Id
    2 - Asset Id
    3 - Name
    4 - Description
    5 - Address 1
    6 - Address 2
    7 - Address 3
    8 - City
    9 - Postcode
    10 - Latitude
    11- Longitude
    */
    public AssetDevice createDevice(String line) throws IOException {
        List<String> values = Arrays.asList(line.split(SEPARATOR_REGEXP));
        AssetDevice device = new AssetDevice();
        String commodity = values.get(0);
        String networkTypeName = values.get(1);
        try {
            networkTypeService.getBy(CommodityType.valueOf(commodity), networkTypeName);
        } catch (IllegalArgumentException e) {
            throw new IOException("Wrong Network: " + commodity + "," + networkTypeName);
        }
        final String externalId = values.get(2);
        if (StringUtils.isNotEmpty(externalId)) {
            device.setExternalId(Long.parseLong(externalId));
        }

        Asset asset = assetService.getByExternalId(Long.parseLong(values.get(3)));
        if (asset == null) {
            asset = assetService.getById(Long.parseLong(values.get(3)));
            if (asset == null) {
                throw new IOException("Wrong Asset ID: " + values.get(3));
            }
        }
        device.setAsset(asset);

        device.setName(unquoteString(values.get(4)));
        device.setDescription(unquoteString(values.get(5)));
        device.setAddress1(unquoteString(values.get(6)));
        device.setAddress2(unquoteString(values.get(7)));
        device.setAddress3(unquoteString(values.get(8)));
        device.setCity(unquoteString(values.get(9)));
        device.setPostcode(unquoteString(values.get(10)));
        if (StringUtils.isNotEmpty(values.get(11))) {
            device.setLatitude(BigDecimal.valueOf(Double.parseDouble(values.get(11))));
        }
        if (StringUtils.isNotEmpty(values.get(12))) {
            device.setLongitude(BigDecimal.valueOf(Double.parseDouble(values.get(12))));
        }

        //import attributes
        int i = 13;
        device.buildInventoryAttributesFromProfile(device.getAsset().getAssetProfile().getAttributes());
        for (DeviceAttribute deviceAttribute : device.getDeviceAttributes()) {
            if (values.size() <= i) {
                if (deviceAttribute.getMandatory()) {
                    throw new IOException("Wrong device attributes count for the record with external id: " + device.getExternalId());
                }
            } else {
                try {
                    deviceAttribute.setAttribute(values.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    throw new IOException("Wrong device attributes count for the record with external id: " + device.getExternalId());
                }
            }
        }

        return device;
    }

    public ByteArrayInputStream downloadDevices(CommodityType commodityType) throws IOException, SQLException {
        return deviceSerializer.dataToByteArrayInputStream(assetDeviceService.getAllByCommodity(commodityType));
    }

    /**
     * 0 - External Id
     * 1 - Source Id
     * 2 - Target Id
     * 3 - Name
     * 4 - Direction
     * 5 - Operation:
     * ADD: Add a source or a target (or both) to an existing link or, if not exists, creates a new one. Multiple lines for the same link could exist
     * DELETE Delete link by external id
     * 6 - Asset Type Profile
     */
    @Transactional
    public DeviceLink createDeviceLink(String line) throws IOException {
        List<String> values = Arrays.asList(line.split(SEPARATOR_REGEXP));
        DeviceLink deviceLink = new DeviceLink();
        String commodity = values.get(0);
        String networkTypeName = values.get(1);
        try {
            networkTypeService.getBy(CommodityType.valueOf(commodity), networkTypeName);
        } catch (IllegalArgumentException e) {
            throw new IOException("Wrong Network: " + commodity + "," + networkTypeName);
        }
        final String sExtId = values.get(2);
        long externalId = -1;
        if (StringUtils.isNotEmpty(sExtId)) {
            externalId = Long.parseLong(sExtId);
            deviceLink.setExternalId(externalId);
        }

        AssetDevice sourceDevice = assetDeviceService.getByExternalId(Long.parseLong(values.get(3)));
        if (sourceDevice == null) {
            sourceDevice = assetDeviceService.getById(Long.parseLong(values.get(3)));
            if (sourceDevice == null) {
                throw new IOException("Wrong Source Device ID: " + values.get(3));
            }
        }
        if (CollectionUtils.isEmpty(deviceLink.getSources())) {
            deviceLink.setSources(new ArrayList<>());
        }
        deviceLink.getSources().add(sourceDevice);

        final String targetId = values.get(4);
        if (StringUtils.isNotEmpty(targetId)) {
            AssetDevice targetDevice = assetDeviceService.getByExternalId(Long.parseLong(targetId));
            if (targetDevice == null) {
                targetDevice = assetDeviceService.getById(Long.parseLong(targetId));
                if (targetDevice == null) {
                    throw new IOException("Wrong Target Device ID: " + targetId);
                }
            }
            if (sourceDevice.getAsset().getAssetProfile().getNetworkType().getCommodityType() != targetDevice.getAsset().getAssetProfile().getNetworkType().getCommodityType()) {
                throw new IOException("Source and Target Devices Must belong to the same Commodity Type. Source ID: " + sourceDevice.getExternalId() + " Commodity: " +
                        sourceDevice.getAsset().getAssetProfile().getNetworkType().getCommodityType().getName() + " Target Id: " + targetDevice.getExternalId() + " Commodity: " +
                        targetDevice.getAsset().getAssetProfile().getNetworkType().getCommodityType()
                );
            }
            if (CollectionUtils.isEmpty(deviceLink.getTargets())) {
                deviceLink.setTargets(new ArrayList<>());
            }
            deviceLink.getTargets().add(targetDevice);
        }

        deviceLink.setName(unquoteString(values.get(5)));

        try {
            DeviceLinkType deviceLinkType = DeviceLinkType.valueOf(values.get(6));
            deviceLink.setType(deviceLinkType);
        } catch (IllegalArgumentException e) {
            throw new IOException("Wrong Device Link Type: " + values.get(6));
        }

        final String assetProfileId = values.get(8);
        final AssetProfile assetProfile = assetProfileService.getById(Long.parseLong(assetProfileId));
        if (assetProfile == null) {
            throw new IOException("Wrong Asset Profile Id: " + values.get(8));
        }
        deviceLink.setAssetProfile(assetProfile);
        deviceLink.buildConnectionAttributesFromProfile(assetProfile.getConnectionProfileAttributes());
        int i = 9;
        for (ConnectionDeviceAttribute attribute : deviceLink.getLinkAttributes()) {
            attribute.setAttribute(values.get(i++));
        }

        final String operation = values.get(7);
        switch (operation) {
            case "ADD": //Add a new source and target to an existing device link
                final DeviceLink linkFromDb = deviceLinkService.getByExternalId(externalId);
                if (linkFromDb == null) {
                    throw new IOException("Link to modify does not exist. Id: " + externalId);
                }
                final List<CommodityType> newCommodityTypes = deviceLink.getDevices().stream().map(l -> l.getAsset().getAssetProfile().getNetworkType().getCommodityType()).distinct().collect(Collectors.toList());
                final List<CommodityType> dbCommodityTypes = linkFromDb.getDevices().stream().map(l -> l.getAsset().getAssetProfile().getNetworkType().getCommodityType()).distinct().collect(Collectors.toList());
                final boolean commoditiesDontMatch = newCommodityTypes.stream().filter(c -> !dbCommodityTypes.contains(c)).findFirst().isPresent();
                if (commoditiesDontMatch) {
                    throw new IOException("Devices Must belong to the same Commodity Type as the already existing devices. Source ID: " + sourceDevice.getExternalId() + " Commodity: " +
                            sourceDevice.getAsset().getAssetProfile().getNetworkType().getCommodityType().getName() + ". Commodities from db: " + dbCommodityTypes.stream().map(c -> c.getName()).collect(Collectors.joining(","))
                    );
                }
                deviceLink.getSources().addAll(linkFromDb.getSources());
                deviceLink.getTargets().addAll(linkFromDb.getTargets());
                deviceLinkService.deleteByExternalId(linkFromDb);
                deviceLinkService.saveOrUpdate(deviceLink);
                break;
            case "DELETE":
                deviceLinkService.deleteByExternalId(deviceLink);
                break;
            default:
                deviceLinkService.saveOrReplace(Collections.singletonList(deviceLink));
                break;
        }
        return deviceLink;
    }

    public ByteArrayInputStream downloadLinks(CommodityType commodityType) throws IOException, SQLException {
        return linkSerializer.dataToByteArrayInputStream(deviceLinkService.getAllByCommodity(commodityType));
    }

    public static String doubleQuotedString(String string) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        return DOUBLE_QUOTE + string + DOUBLE_QUOTE;
    }

    public static String unquoteString(String string) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        if (string.startsWith(DOUBLE_QUOTE) && string.endsWith(DOUBLE_QUOTE)) {
            if (string.length() <= 2) {
                return StringUtils.EMPTY;
            }
            return string.substring(1, string.length() - 1);
        }
        return string;
    }
}