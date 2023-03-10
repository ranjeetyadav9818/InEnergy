package com.inenergis.service;

import com.inenergis.dao.ManufacturerDao;
import com.inenergis.entity.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ManufacturerServiceTest {

    @Mock
    private ManufacturerDao manufacturerDao;

    @InjectMocks
    private ManufacturerService manufacturerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save() {
        manufacturerService.save(new Manufacturer());
        Mockito.verify(manufacturerDao).save(Mockito.any(Manufacturer.class));
    }

    @Test
    void getAll() {
        manufacturerService.getAll();
        Mockito.verify(manufacturerDao).getAll();
    }

    @Test
    void getById() {
        manufacturerService.getById(1L);
        Mockito.verify(manufacturerDao).getById(1L);
    }
}