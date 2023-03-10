package com.inenergis.controller.asset;

import com.inenergis.controller.carousel.AssetCarousel;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.CatalogProfileAttribute;
import com.inenergis.entity.assetTopology.DeviceProfileAttribute;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.DocumentService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.service.NoteService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetDetailsControllerTest {

    @Mock
    private ContractEntityService contractEntityService;

    @Mock
    private AssetService assetService;

    @Mock
    private AssetCarousel assetCarousel;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private AssetProfileService assetProfileService;

    @Mock
    private AssetGroupService assetGroupService;

    @Mock
    private DocumentService documentService;

    @Mock
    private NoteService noteService;

    @InjectMocks
    private AssetDetailsController assetDetailsController;

    @BeforeEach
    void setUp() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setAssetProfile(new AssetProfile());
        asset.getAssetProfile().setAttributes(Arrays.asList(new CatalogProfileAttribute(), new DeviceProfileAttribute()));
        MockitoAnnotations.initMocks(this);
        assetDetailsController.setAsset(asset);
        assetDetailsController.setAssetCarousel(assetCarousel);
        assetDetailsController.setEntityDetails(new ArrayList<>());
    }

    @Test
    void init() {
        assetDetailsController.doInit();
        Assertions.assertNotNull(assetDetailsController.asset);
    }

    @Test
    void editAsset() {
        assetDetailsController.editAsset();
        assertTrue(assetDetailsController.isEditMode());
    }

    @Test
    void save() throws IOException {
        UIMessage uiMessage = Mockito.mock(UIMessage.class);
        final Asset asset = new Asset();
        asset.setName("name");
        Mockito.when(assetService.saveOrUpdate(Mockito.any())).thenReturn(asset);
        assetDetailsController.setUiMessage(uiMessage);
        assetDetailsController.doSave();
        Mockito.verify(assetService).saveOrUpdate(Mockito.any());
        assertFalse(assetDetailsController.isEditMode());
        Mockito.verify(assetCarousel).generate(Mockito.anyList(), Mockito.any());
    }

    @Test
    void clear() {
        assetDetailsController.clear();
        assertFalse(assetDetailsController.isEditMode());
    }

    @Test
    void completeEntity() {
        assetDetailsController.completeEntity("query");
        Mockito.verify(contractEntityService).getByBusinessName("query");
    }
}