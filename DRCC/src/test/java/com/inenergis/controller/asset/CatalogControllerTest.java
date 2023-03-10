package com.inenergis.controller.asset;

import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.ManufacturerService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.primefaces.event.RowEditEvent;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CatalogControllerTest {

    @Mock
    private ContractEntityService contractEntityService;

    @Mock
    private AssetService assetService;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private AssetProfileService assetProfileService;

    @Mock
    private AssetGroupService assetGroupService;

    @Mock
    private RowEditEvent rowEditEvent;

    @Mock
    private UIMessage uiMessage;

    @Mock
    private NetworkType networkType;

    @InjectMocks
    private CatalogController catalogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(networkType.getId()).thenReturn(1L);
        Mockito.when(networkType.getCommodityType()).thenReturn(CommodityType.ELECTRICITY);
    }

    @Test
    void init() {
        catalogController.doInit();
        catalogController.search();
        assertNotNull(catalogController.getLazyAssetDataModel());
        Mockito.verify(contractEntityService).getAll();
    }

    @Test
    void search() {
        catalogController.search();
        assertNotNull(catalogController.getLazyAssetDataModel());
    }

    @Test
    void save() throws IOException {
        Asset asset = new Asset();
        asset.setName("Test 1");
        catalogController.setAsset(asset);
        try{
            catalogController.save();
        } catch (NullPointerException e){

        } catch (Exception e){
            fail("Unexpected exception");
        }
        Mockito.verify(assetService).saveOrUpdate(asset);
        Mockito.verify(uiMessage).addMessage(Mockito.any(), Mockito.contains(asset.getName()));
    }

    @Test
    void clear() {
        catalogController.clear();
        assertFalse(catalogController.isEditMode());
    }

    @Test
    void add() {
        catalogController.add();
        assertNotNull(catalogController.getAsset());
        assertTrue(catalogController.isEditMode());
    }

    @Test
    void update() {
        Asset asset = new Asset();
        asset.setName("Test 1");
        catalogController.update(asset);
        assertEquals(asset, catalogController.getAsset());
        assertTrue(catalogController.isEditMode());
    }

    @Test
    void completeEntity() {
        catalogController.completeEntity("query");
        Mockito.verify(contractEntityService).getByBusinessName("query");
    }

    @Test
    void onRowEdit() throws IOException {
        catalogController.onRowEdit(rowEditEvent);
        Mockito.verify(rowEditEvent).getObject();
        Mockito.verify(assetService).saveOrUpdate(Mockito.any());
    }

    @Test
    void onRowCancel() {
        catalogController.onRowCancel(rowEditEvent);
        Mockito.verify(rowEditEvent).getObject();
    }
}