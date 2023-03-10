package com.inenergis.controller.general;

import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.CatalogProfileAttribute;
import com.inenergis.entity.assetTopology.ConnectionDeviceAttribute;
import com.inenergis.entity.assetTopology.ConnectionProfileAttribute;
import com.inenergis.entity.assetTopology.DeviceAttribute;
import com.inenergis.entity.assetTopology.DeviceProfileAttribute;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.entity.genericEnum.AssetOwnership;
import com.inenergis.entity.genericEnum.AssetUsage;
import com.inenergis.entity.genericEnum.AttributeValidation;
import com.inenergis.entity.genericEnum.CommodityType;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetHelperTest {

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private ContractEntityService contractEntityService;

    @Mock
    private AssetProfile assetProfile;

    @Mock
    private AssetProfileService assetProfileService;

    @Mock
    private AssetGroupService assetGroupService;

    @Mock
    private AssetService assetService;

    @Mock
    private AssetDeviceService assetDeviceService;

    @Mock
    private DeviceLinkService deviceLinkService;

    @Mock
    private NetworkType networkType;

    @Mock
    private ContractEntity contractEntity;

    @Mock
    private Manufacturer manufacturer;

    @Mock
    private AssetGroup assetGroup;

    @Mock
    private AssetDevice device;

    @Mock
    private AssetDevice deviceTarget;

    @Mock
    private Asset asset;

    @Mock
    private NetworkTypeService networkTypeService;

    @Mock
    private PropertyAccessor propertyAccessor;

    @InjectMocks
    private AssetHelper assetHelper;

    private static final String testLineBTM = "ELECTRICITY,Behind The Meter,2,\"Catalog 1 Name\",\"Catalog 1 Description\",\"Sony\",1,9954,1,1234567890,LZ229,10021,10016,CUSTOMER,CONSUMPTION,123,Text,2017-07-25";
    private static final String testLineDistributionAsset = "ELECTRICITY,Distribution,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";
    private static final String testLineTransmissionAsset = "ELECTRICITY,Transmission,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";
    private static final String testLineTransportationAsset = "GAS,Transportation,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";
    private static final String testLineGasDistributionAsset = "GAS,Distribution,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";

    private static final String testLineWrongType = "FANTA,Transportation,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";
    private static final String testLineWrongManufacturerId = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,1234,1,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";
    private static final String testLineWrongSupplierId = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,2,1234567890,LZ229,10021,10016,,,123,Text,2017-07-25";
    private static final String testLineWrongAssetProfileId = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,1234,10016,CUSTOMER,CONSUMPTION,123,Text,2017-07-25";
    private static final String testLineWrongAssetGroupId = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,1234,CUSTOMER,CONSUMPTION,123,Text,2017-07-25";
    private static final String testLineWrongOwnership = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,WRONG_OWNERSHIP,CONSUMPTION,123,Text,2017-07-25";
    private static final String testLineWrongUsage = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,CUSTOMER,WRONG_USAGE,123,Text,2017-07-25";
    private static final String testLineWrongAttributesCount = "ELECTRICITY,Behind The Meter,2,Catalog 1 Name,Catalog 1 Description,Sony,1,9954,1,1234567890,LZ229,10021,10016,CUSTOMER,CONSUMPTION,123,Text";

    private static final String testLineBTMDevice = "ELECTRICITY,Behind The Meter,1,10180,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";
    private static final String testLineDistributionDevice = "ELECTRICITY,Distribution,1,10181,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";
    private static final String testLineTransmissionDevice = "ELECTRICITY,Transmission,1,10182,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";
    private static final String testLineTransportationDevice = "ELECTRICITY,Transportation,1,10183,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";
    private static final String testLineGasDistributionDevice = "ELECTRICITY,Distribution,1,10184,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";

    private static final String testLineDeviceWrongType = "FANTA,WRONG_TYPE,1,10180,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";
    private static final String testLineDeviceWrongAssetProfileId = "ELECTRICITY,Behind The Meter,1,1234,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123,Text attribute 1,2017-07-25";
    private static final String testLineDeviceWrongAttributesCount = "ELECTRICITY,Behind The Meter,1,10180,Device Name A,Device Description A,Line 1,Line 2, Line2, London, SE12 0PL,23,29.990,123";
    private static final String testLineDeviceLink = "ELECTRICITY,Behind The Meter,1,2,3,Connection One,IN,,10021,1";
    private static final String testLineDeviceLinkAdd = "ELECTRICITY,Behind The Meter,1,2,3,Connection One,IN,ADD,10021,1";
    private static final String testLineDeviceLinkWithWrongSourceId = "ELECTRICITY,Behind The Meter,1,123,3,Connection One,IN,,10021,1";
    private static final String testLineDeviceLinkWithWrongTargetId = "ELECTRICITY,Behind The Meter,1,2,123,Connection One,IN,,10021,1";
    private static final String testLineDeviceLinkWithWrongDeviceLinkType = "ELECTRICITY,Behind The Meter,1,2,3,Connection One,WRONG_LINK_TYPE,,10021,1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(manufacturer.getId()).thenReturn(9954L);
        Mockito.when(contractEntity.getId()).thenReturn(1L);
        Mockito.when(manufacturerService.getById(9954L)).thenReturn(manufacturer);
        Mockito.when(contractEntityService.getById(1L)).thenReturn(contractEntity);
        DeviceProfileAttribute deviceProfileAttribute1 = new DeviceProfileAttribute();
        DeviceProfileAttribute deviceProfileAttribute2 = new DeviceProfileAttribute();
        DeviceProfileAttribute deviceProfileAttribute3 = new DeviceProfileAttribute();
        CatalogProfileAttribute catalogProfileAttribute1 = new CatalogProfileAttribute();
        CatalogProfileAttribute catalogProfileAttribute2 = new CatalogProfileAttribute();
        CatalogProfileAttribute catalogProfileAttribute3 = new CatalogProfileAttribute();

        deviceProfileAttribute1.setMandatory(true);
        deviceProfileAttribute2.setMandatory(true);
        deviceProfileAttribute3.setMandatory(true);
        catalogProfileAttribute1.setMandatory(true);
        catalogProfileAttribute2.setMandatory(true);
        catalogProfileAttribute3.setMandatory(true);

        deviceProfileAttribute1.setAttributeValidation(AttributeValidation.NUMBER);
        deviceProfileAttribute2.setAttributeValidation(AttributeValidation.TEXT);
        deviceProfileAttribute3.setAttributeValidation(AttributeValidation.DATE);
        catalogProfileAttribute1.setAttributeValidation(AttributeValidation.NUMBER);
        catalogProfileAttribute2.setAttributeValidation(AttributeValidation.TEXT);
        catalogProfileAttribute3.setAttributeValidation(AttributeValidation.DATE);
        Mockito.when(assetProfile.getCatalogProfileAttributes()).thenReturn(Arrays.asList(catalogProfileAttribute1, catalogProfileAttribute2, catalogProfileAttribute3));
        Mockito.when(assetProfile.getDeviceProfileAttributes()).thenReturn(Arrays.asList(deviceProfileAttribute1, deviceProfileAttribute2, deviceProfileAttribute3));
        Mockito.when(assetProfile.getAttributes()).thenReturn(Arrays.asList(deviceProfileAttribute1, deviceProfileAttribute2, deviceProfileAttribute3, catalogProfileAttribute1, catalogProfileAttribute2, catalogProfileAttribute3));
        Mockito.when(assetProfileService.getById(10021L)).thenReturn(assetProfile);
        Mockito.when(assetProfile.getId()).thenReturn(10021L);
        Mockito.when(assetGroupService.getById(10016L)).thenReturn(assetGroup);
        Mockito.when(assetGroup.getId()).thenReturn(10016L);
        asset.setAssetProfile(assetProfile);
        Asset distributionAsset = new Asset();
        distributionAsset.setAssetProfile(assetProfile);
        Asset transmissionAsset = new Asset();
        transmissionAsset.setAssetProfile(assetProfile);
        Asset gasDistributionAsset = new Asset();
        gasDistributionAsset.setAssetProfile(assetProfile);
        Asset transportationAsset = new Asset();
        transportationAsset.setAssetProfile(assetProfile);
        Mockito.when(assetService.getByExternalId(10180L)).thenReturn(asset);
        Mockito.when(assetService.getByExternalId(10181L)).thenReturn(distributionAsset);
        Mockito.when(assetService.getByExternalId(10182L)).thenReturn(transmissionAsset);
        Mockito.when(assetService.getByExternalId(10183L)).thenReturn(transportationAsset);
        Mockito.when(assetService.getByExternalId(10184L)).thenReturn(gasDistributionAsset);
        Mockito.when(assetService.getById(10016L)).thenReturn(asset);//It can be looked for asset id or external id
        Mockito.when(device.getId()).thenReturn(10016L);
        Mockito.when(asset.getId()).thenReturn(10016L);
        Mockito.when(asset.getExternalId()).thenReturn(10180L);
        Mockito.when(asset.getAssetProfile()).thenReturn(assetProfile);
        Mockito.when(device.getAsset()).thenReturn(asset);
        Mockito.when(deviceTarget.getAsset()).thenReturn(asset);

        device.setAsset(asset);
        deviceTarget.setAsset(asset);
        Mockito.when(assetDeviceService.getByExternalId(2L)).thenReturn(device);
        Mockito.when(device.getExternalId()).thenReturn(2L);
        Mockito.when(assetDeviceService.getByExternalId(3L)).thenReturn(deviceTarget);
        Mockito.when(deviceTarget.getExternalId()).thenReturn(3L);
        DeviceLink deviceLink = new DeviceLink();
        deviceLink.getSources().add(device);
        deviceLink.getTargets().add(deviceTarget);
        Mockito.when(deviceLinkService.getByExternalIds(Collections.singletonList(1L))).thenReturn(Collections.singletonList(deviceLink));
        Mockito.when(deviceLinkService.getByExternalId(1L)).thenReturn(deviceLink);
        Mockito.when(assetProfile.getNetworkType()).thenReturn(networkType);
        Mockito.when(networkType.getCommodityType()).thenReturn(CommodityType.ELECTRICITY);
        Mockito.when(networkType.getName()).thenReturn("Behind The Meter");
        ConnectionProfileAttribute connectionProfileAttribute = new ConnectionProfileAttribute();
        connectionProfileAttribute.setAssetProfile(assetProfile);
        connectionProfileAttribute.setAttributeValidation(AttributeValidation.NUMBER);
        Mockito.when(assetProfile.getConnectionProfileAttributes()).thenReturn(Collections.singletonList(connectionProfileAttribute));

        Mockito.when(networkTypeService.getBy(CommodityType.ELECTRICITY, "Transmission")).thenReturn(NetworkType.builder().commodityType(CommodityType.ELECTRICITY).name("Transmission").build());
        Mockito.when(networkTypeService.getBy(CommodityType.ELECTRICITY, "Distribution")).thenReturn(NetworkType.builder().commodityType(CommodityType.ELECTRICITY).name("Distribution").build());
        Mockito.when(networkTypeService.getBy(CommodityType.ELECTRICITY, "Behind The Meter")).thenReturn(NetworkType.builder().commodityType(CommodityType.ELECTRICITY).name("Behind The Meter").build());

        Mockito.when(networkTypeService.getBy(CommodityType.GAS, "Transportation")).thenReturn(NetworkType.builder().commodityType(CommodityType.GAS).name("Transportation").build());
        Mockito.when(networkTypeService.getBy(CommodityType.GAS, "Distribution")).thenReturn(NetworkType.builder().commodityType(CommodityType.GAS).name("Distribution").build());

    }

    @Test
    void createBTMAsset() throws IOException {
        assertNotNull(assetHelper.createAsset(testLineBTM));
    }

    @Test
    void createDistributionAsset() throws IOException {
        assertNotNull(assetHelper.createAsset(testLineDistributionAsset));
    }

    @Test
    void createTransmissionAsset() throws IOException {
        assertNotNull(assetHelper.createAsset(testLineTransmissionAsset));
    }

    @Test
    void createTransportationAsset() throws IOException {
        assertNotNull(assetHelper.createAsset(testLineTransportationAsset));
    }

    @Test
    void createGasDistributionAsset() throws IOException {
        assertNotNull(assetHelper.createAsset(testLineGasDistributionAsset));
    }

    @Test
    void createAssetWithWrongType() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongType));
        assertEquals("Wrong Network: FANTA,Transportation", exception.getMessage());
    }

    @Test
    void createAssetWithWrongManufacturerId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongManufacturerId));
        assertEquals("Wrong Manufacturer ID: 1234", exception.getMessage());
    }

    @Test
    void createAssetWithWrongSupplierId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongSupplierId));
        assertEquals("Wrong Supplier ID: 2", exception.getMessage());
    }

    @Test
    void createAssetWithWrongAssetProfileId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongAssetProfileId));
        assertEquals("Wrong Asset Profile ID: 1234", exception.getMessage());
    }

    @Test
    void createAssetWithWrongAssetGroupId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongAssetGroupId));
        assertEquals("Wrong Asset Group ID: 1234", exception.getMessage());
    }

    @Test
    void createAssetWithWrongOwnership() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongOwnership));
        assertEquals("Wrong Ownership: WRONG_OWNERSHIP", exception.getMessage());
    }

    @Test
    void createAssetWithWrongUsage() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongUsage));
        assertEquals("Wrong Usage: WRONG_USAGE", exception.getMessage());
    }

    @Test
    void createAssetWithWrongAttributesCount() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createAsset(testLineWrongAttributesCount));
        assertEquals("Wrong attributes count for the record with external id: 2", exception.getMessage());
    }

    @Test
    void assetHasName() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals("Catalog 1 Name", asset.getName());
    }

    @Test
    void assetHasDescription() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals("Catalog 1 Description", asset.getDescription());
    }

    @Test
    void assetHasMake() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals("Sony", asset.getMake());
    }

    @Test
    void assetIsActive() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertTrue(asset.isActive());
    }

    @Test
    void assetHasManufacturer() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertNotNull(asset.getManufacturer());
    }

    @Test
    void assetHasSupplier() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertNotNull(asset.getSupplier());
    }

    @Test
    void assetHasSupplierPartNumber() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals("1234567890", asset.getSupplierPartNumber());
    }

    @Test
    void assetHasModel() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals("LZ229", asset.getModel());
    }

    @Test
    void assetHasAssetProfile() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertNotNull(asset.getAssetProfile());
    }

    @Test
    void assetHasGroupId() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertNotNull(asset.getAssetGroup());
    }

    @Test
    void assetHasOwnership() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals(AssetOwnership.CUSTOMER, (asset.getOwnership()));
    }

    @Test
    void assetHasUsage() throws IOException {
        Asset asset = assetHelper.createAsset(testLineBTM);
        assertEquals(AssetUsage.CONSUMPTION, asset.getUsage());
    }

    @Test
    void createBTMDevice() throws IOException {
        assertNotNull(assetHelper.createDevice(testLineBTMDevice));
    }

    @Test
    void createDistributionDevice() throws IOException {
        assertNotNull(assetHelper.createDevice(testLineDistributionDevice));
    }

    @Test
    void createTransmissionDevice() throws IOException {
        assertNotNull(assetHelper.createDevice(testLineTransmissionDevice));
    }

    @Test
    void createTransportationDevice() throws IOException {
        assertNotNull(assetHelper.createDevice(testLineTransportationDevice));
    }

    @Test
    void createGasDistributionDevice() throws IOException {
        assertNotNull(assetHelper.createDevice(testLineGasDistributionDevice));
    }

    @Test
    void createDeviceWithWrongType() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createDevice(testLineDeviceWrongType));
        assertEquals("Wrong Network: FANTA,WRONG_TYPE", exception.getMessage());
    }

    @Test
    void createDeviceWithWrongAssetProfileId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createDevice(testLineDeviceWrongAssetProfileId));
        assertEquals("Wrong Asset ID: 1234", exception.getMessage());
    }

    @Test
    void createDeviceWithWrongAttributesCount() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createDevice(testLineDeviceWrongAttributesCount));
        assertEquals("Wrong device attributes count for the record with external id: 1", exception.getMessage());
    }

    @Test
    void createDeviceLink() throws IOException {
        Mockito.when(networkType.getCommodityType()).thenReturn(CommodityType.ELECTRICITY);
        assertNotNull(assetHelper.createDeviceLink(testLineDeviceLink));
    }

    @Test
    void createDeviceLinkAdd() throws IOException {
        Mockito.when(networkType.getCommodityType()).thenReturn(CommodityType.ELECTRICITY);
        final DeviceLink deviceLink;
        deviceLink = assetHelper.createDeviceLink(testLineDeviceLinkAdd);
        assertNotNull(deviceLink);
        assertFalse(CollectionUtils.isEmpty(deviceLink.getSources()));
        assertFalse(CollectionUtils.isEmpty(deviceLink.getTargets()));
        assertTrue(deviceLink.getSources().size() == 2);
        assertTrue(deviceLink.getTargets().size() == 2);
    }

    @Test
    void createDeviceLinkWithWrongSourceId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createDeviceLink(testLineDeviceLinkWithWrongSourceId));
        assertEquals("Wrong Source Device ID: 123", exception.getMessage());
    }

    @Test
    void createDeviceLinkWithWrongTargetId() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createDeviceLink(testLineDeviceLinkWithWrongTargetId));
        assertEquals("Wrong Target Device ID: 123", exception.getMessage());
    }

    @Test
    void createDeviceLinkWithWrongDeviceLinkType() throws IOException {
        Throwable exception = assertThrows(IOException.class, () -> assetHelper.createDeviceLink(testLineDeviceLinkWithWrongDeviceLinkType));
        assertEquals("Wrong Device Link Type: WRONG_LINK_TYPE", exception.getMessage());
    }

    @Test
    void downloadAsset() throws IOException, SQLException {
        final Asset asset = assetHelper.createAsset(testLineBTM);
        AssetSerializer serializer = new AssetSerializer();
        final ByteArrayInputStream stream = serializer.dataToByteArrayInputStream(Collections.singletonList(asset));
        final int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer, 0, size);
        String downloaded = new String(buffer, UTF_8);
        final String[] headerAndLine = downloaded.split("\n");
        assertEquals(testLineBTM, headerAndLine[1]);
    }

    @Test
    void downloadDevice() throws IOException, SQLException {
        final AssetDevice device = assetHelper.createDevice(testLineBTMDevice);
        DeviceSerializer serializer = new DeviceSerializer();
        final ByteArrayInputStream stream = serializer.dataToByteArrayInputStream(Collections.singletonList(device));
        final int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer, 0, size);
        String downloaded = new String(buffer, UTF_8);
        final String[] headerAndLine = downloaded.split("\n");
        final String line = headerAndLine[1];
        final AssetDevice deviceDownloaded = assetHelper.createDevice(line);
        assertEquals(device.getName(), deviceDownloaded.getName());
        assertEquals(device.getDescription(), deviceDownloaded.getDescription());
        assertEquals(device.getExternalId(), deviceDownloaded.getExternalId());
        assertEquals(device.getAddress1(), deviceDownloaded.getAddress1());
        assertEquals(device.getAddress2(), deviceDownloaded.getAddress2());
        assertEquals(device.getAddress3(), deviceDownloaded.getAddress3());
        assertEquals(device.getCity(), deviceDownloaded.getCity());
        assertEquals(device.getPostcode(), deviceDownloaded.getPostcode());
        assertEquals(device.getLatitude(), deviceDownloaded.getLatitude());
        assertEquals(device.getLongitude(), deviceDownloaded.getLongitude());
        assertEquals(device.getAsset(), deviceDownloaded.getAsset());
        assertNotNull(deviceDownloaded.getDeviceAttributes());
        assertEquals(device.getDeviceAttributes().size(), deviceDownloaded.getDeviceAttributes().size());
        for (int i = 0; i < device.getDeviceAttributes().size(); i++) {
            final DeviceAttribute expected = device.getDeviceAttributes().get(i);
            final DeviceAttribute actual = deviceDownloaded.getDeviceAttributes().get(i);
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getDateValue(), actual.getDateValue());
            assertEquals(expected.getStringValue(), actual.getStringValue());
            assertEquals(expected.getNumberValue(), actual.getNumberValue());
        }
    }

    @Test
    void downloadConnection() throws IOException, SQLException {
        final DeviceLink deviceLink = assetHelper.createDeviceLink(testLineDeviceLinkAdd);
        LinkSerializer serializer = new LinkSerializer();
        final ByteArrayInputStream stream = serializer.dataToByteArrayInputStream(Collections.singletonList(deviceLink));
        final int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer, 0, size);
        String downloaded = new String(buffer, UTF_8);
        final String[] headerAndLine = downloaded.split("\n");
        final String line = headerAndLine[1];
        final DeviceLink downloadedLink = assetHelper.createDeviceLink(line);
        assertEquals(deviceLink.getName(), downloadedLink.getName());
        assertEquals(deviceLink.getType(), downloadedLink.getType());
        assertNotNull(downloadedLink.getLinkAttributes());
        assertEquals(deviceLink.getLinkAttributes().size(), downloadedLink.getLinkAttributes().size());
        assertEquals(deviceLink.getExternalId(), downloadedLink.getExternalId());
        for (int i = 0; i < deviceLink.getLinkAttributes().size(); i++) {
            final ConnectionDeviceAttribute expected = deviceLink.getLinkAttributes().get(i);
            final ConnectionDeviceAttribute actual = downloadedLink.getLinkAttributes().get(i);
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getDateValue(), actual.getDateValue());
            assertEquals(expected.getStringValue(), actual.getStringValue());
            assertEquals(expected.getNumberValue(), actual.getNumberValue());
        }

        checkDevices(deviceLink.getSources(), downloadedLink.getSources());
        checkDevices(deviceLink.getTargets(), downloadedLink.getTargets());
    }

    private void checkDevices(List<AssetDevice> originalDevices, List<AssetDevice> finalDevices) {
        assertNotNull(originalDevices);
        assertNotNull(finalDevices);

        assertFalse(originalDevices.isEmpty());
        assertFalse(finalDevices.isEmpty());

        final Long numOriginSourcesNotPresentAtCopy = originalDevices.stream().filter(dl -> !finalDevices.contains(dl)).count();

        assertEquals(0L, numOriginSourcesNotPresentAtCopy.longValue());

        final Long numCopySourcesNotPresentAtOrigin = finalDevices.stream().filter(dl -> !originalDevices.contains(dl)).count();
        assertEquals(0L, numCopySourcesNotPresentAtOrigin.longValue());
    }


}