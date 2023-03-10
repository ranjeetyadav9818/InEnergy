package com.inenergis.service;

import com.inenergis.dao.ServiceAgreementDao;
import com.inenergis.entity.ServiceAgreement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceAgreementServiceTest {

    @Mock
    private ServiceAgreementDao serviceAgreementDao;

    @InjectMocks
    private ServiceAgreementService serviceAgreementService = new ServiceAgreementService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(serviceAgreementDao.getById(Mockito.anyString())).thenReturn(new ServiceAgreement());

        Mockito.when(serviceAgreementDao.getAllByIds(Collections.singletonList("1")))
                .thenReturn(Collections.singletonList(new ServiceAgreement()));

        Mockito.when(serviceAgreementDao.getAllByIds(Arrays.asList("1", "2")))
                .thenReturn(Arrays.asList(new ServiceAgreement(), new ServiceAgreement()));

        Mockito.when(serviceAgreementDao.getDistinctHas3rdPartyValues()).thenReturn(Arrays.asList("Value 1", "Value 2"));
        Mockito.when(serviceAgreementDao.getDistinctCustClassCdValues()).thenReturn(Arrays.asList("Value 3", "Value 4"));
        Mockito.when(serviceAgreementDao.getDistinctCustSizeValues()).thenReturn(Arrays.asList("Value 5", "Value 6"));
    }

    @Test
    void getListByIdSingle() {
        assertEquals(1, serviceAgreementService.getListById("1").size());
    }

    @Test
    void getListByIdMultiple() {
        assertEquals(2, serviceAgreementService.getListById("1", "2").size());
    }

    @Test
    void getById() {
        assertEquals(ServiceAgreement.class, serviceAgreementService.getById("1").getClass());
    }

    @Test
    void getAllBy() {
        assertEquals(2, serviceAgreementService.getAllById(Arrays.asList("1", "2")).size());
    }

    @Test
    void getDistinctHas3rdPartyValues() {
        assertEquals(Arrays.asList("Value 1", "Value 2"), serviceAgreementService.getDistinctHas3rdPartyValues());
    }

    @Test
    void getDistinctCustClassCdValues() {
        assertEquals(Arrays.asList("Value 3", "Value 4"), serviceAgreementService.getDistinctCustClassCdValues());
    }

    @Test
    void getDistinctCustSizeValues() {
        assertEquals(Arrays.asList("Value 5", "Value 6"), serviceAgreementService.getDistinctCustSizeValues());
    }
}