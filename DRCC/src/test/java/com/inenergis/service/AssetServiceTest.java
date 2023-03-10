package com.inenergis.service;

import com.inenergis.dao.AssetDao;
import com.inenergis.entity.asset.Asset;
import com.inenergis.util.ElasticActionsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class AssetServiceTest {

    @Mock
    private AssetDao assetDao;

    @InjectMocks
    private AssetService assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveOrUpdate() throws IOException {
        Asset asset = new Asset();
        asset.setName("Test 1");
        asset.setId(1L);
        ElasticActionsUtil elasticActionsUtil= Mockito.mock(ElasticActionsUtil.class);
        assetService.setElasticActionsUtil(elasticActionsUtil);
        Mockito.when(assetDao.saveOrUpdate(Mockito.any())).thenReturn(asset);
        assetService.saveOrUpdate(asset);
        Mockito.verify(assetDao).saveOrUpdate(asset);
    }

    @Test
    void getAll() {
        assetService.getAll();
        Mockito.verify(assetDao).getAll();
    }

    @Test
    void getById() {
        assetService.getById(1L);
        Mockito.verify(assetDao).getById(1L);
    }
}