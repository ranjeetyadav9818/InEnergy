package com.inenergis.controller.asset;

import com.inenergis.controller.general.AssetHelper;
import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.DeviceLinkService;
import com.inenergis.service.DeviceService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

class CatalogAndInventoriesImportExportControllerTest {

    @Mock
    private AssetHelper assetHelper;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private ContractEntityService contractEntityService;

    @Mock
    private AssetProfileService assetProfileService;

    @Mock
    private AssetGroupService assetGroupService;

    @Mock
    private UIMessage uiMessage;

    @Mock
    private AssetService assetService;

    @Mock
    private DeviceService deviceService;

    @Mock
    private DeviceLinkService deviceLinkService;

    @Mock
    private DefaultUploadedFile uploadedFile;

    @Mock
    private FileUploadEvent event;

    @Mock
    private UIComponent uiComponent;

    @InjectMocks
    private CatalogAndInventoriesImportExportController controller;

    private String oneLineString = "External Id,Source Id,Target Id,Name,Direction";
    private String twoLinesString = "External Id,Source Id,Target Id,Name,Direction\n1,2,3,Connection One,IN";

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        controller.setCommodityType(CommodityType.ELECTRICITY);
        Mockito.when(manufacturerService.getById(Mockito.anyLong())).thenReturn(new Manufacturer());
        Mockito.when(contractEntityService.getById(Mockito.anyLong())).thenReturn(new ContractEntity());
        Mockito.when(assetProfileService.getById(Mockito.anyLong())).thenReturn(new AssetProfile());
        Mockito.when(assetGroupService.getById(Mockito.anyLong())).thenReturn(new AssetGroup());
        Mockito.when(event.getComponent()).thenReturn(uiComponent);
        Mockito.when(uploadedFile.getFileName()).thenReturn("test.csv");
        Mockito.when(event.getFile()).thenReturn(uploadedFile);
        Mockito.when(uploadedFile.getInputstream()).thenReturn(new ByteArrayInputStream("121212".getBytes(StandardCharsets.UTF_8)));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", "assets");
        Mockito.when(uiComponent.getAttributes()).thenReturn(attributes);
    }

    @Test
    void submit() throws IOException {
        controller.submit(event);
        Mockito.verify(assetService).saveOrReplace(Mockito.anyList());
        Mockito.verify(deviceService).saveOrReplace(Mockito.anyList());
        Mockito.verify(deviceLinkService, times(0)).saveOrReplace(Mockito.anyList());
    }

    @Test
    void submitWithoutFile() throws IOException {
        Mockito.when(event.getFile()).thenReturn(null);
        controller.submit(event);
        Mockito.verify(uiMessage).addMessage("file is mandatory", FacesMessage.SEVERITY_ERROR);
    }

    @Test
    void submitWithEmptyFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        Mockito.when(uploadedFile.getInputstream()).thenReturn(inputStream);
        Throwable exception = assertThrows(IOException.class, () -> controller.submit(event));
        assertEquals("empty file", exception.getMessage());
    }

    @Test
    void submitWithOneLineFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(oneLineString.getBytes(StandardCharsets.UTF_8));
        Mockito.when(uploadedFile.getInputstream()).thenReturn(inputStream);
        controller.submit(event);
        Mockito.verify(uiMessage).addMessage(CatalogAndInventoriesImportExportController.RECORDS_PROCESSED_FROM_FILE_1, 0, uploadedFile.getFileName());
    }

    @Test
    void submitWithTwoLineFileForAssets() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(twoLinesString.getBytes(StandardCharsets.UTF_8));
        Mockito.when(uploadedFile.getInputstream()).thenReturn(inputStream);
        controller.submit(event);
        Mockito.verify(uiMessage).addMessage(CatalogAndInventoriesImportExportController.RECORDS_PROCESSED_FROM_FILE_1, 1, uploadedFile.getFileName());
    }

    @Test
    void submitWithTwoLineFileForDevices() throws IOException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", "devices");
        Mockito.when(uiComponent.getAttributes()).thenReturn(attributes);

        InputStream inputStream = new ByteArrayInputStream(twoLinesString.getBytes(StandardCharsets.UTF_8));
        Mockito.when(uploadedFile.getInputstream()).thenReturn(inputStream);
        controller.submit(event);
        Mockito.verify(uiMessage).addMessage(CatalogAndInventoriesImportExportController.RECORDS_PROCESSED_FROM_FILE_1, 1, uploadedFile.getFileName());
    }

    @Test
    void submitWithTwoLineFileForLinks() throws IOException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", "links");
        Mockito.when(uiComponent.getAttributes()).thenReturn(attributes);

        InputStream inputStream = new ByteArrayInputStream(twoLinesString.getBytes(StandardCharsets.UTF_8));
        Mockito.when(uploadedFile.getInputstream()).thenReturn(inputStream);
        controller.submit(event);
        Mockito.verify(uiMessage).addMessage(CatalogAndInventoriesImportExportController.RECORDS_PROCESSED_FROM_FILE_1, 1, uploadedFile.getFileName());
    }
}