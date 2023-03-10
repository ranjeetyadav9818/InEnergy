package com.inenergis.service;

import com.inenergis.dao.ServiceAgreementAssetDao;
import com.inenergis.entity.ServiceAgreementAsset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ServiceAgreementAssetServiceTest {

    @Mock
    private ServiceAgreementAssetDao serviceAgreementAssetDao;

    @InjectMocks
    private ServiceAgreementAssetService serviceAgreementAssetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getById() {
        serviceAgreementAssetService.getById(1L);
        Mockito.verify(serviceAgreementAssetDao).getById(1L);
    }

    @Test
    void save() {
        ServiceAgreementAsset serviceAgreementAsset = new ServiceAgreementAsset();
        serviceAgreementAssetService.save(serviceAgreementAsset);
        Mockito.verify(serviceAgreementAssetDao).saveOrUpdate(serviceAgreementAsset);
    }

    @Test
    void delete() {
        ServiceAgreementAsset serviceAgreementAsset = new ServiceAgreementAsset();
        serviceAgreementAssetService.delete(serviceAgreementAsset);
        Mockito.verify(serviceAgreementAssetDao).delete(serviceAgreementAsset);
    }
}