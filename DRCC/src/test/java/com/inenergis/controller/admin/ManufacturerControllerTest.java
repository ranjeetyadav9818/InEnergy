package com.inenergis.controller.admin;

import com.inenergis.entity.Manufacturer;
import com.inenergis.service.ManufacturerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManufacturerControllerTest {

    @Mock
    private ManufacturerService manufacturerService;

    @InjectMocks
    private ManufacturerController manufacturerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void init() {
        manufacturerController.init();
        Mockito.verify(manufacturerService).getAll();
        assertNotNull(manufacturerController.getList());
    }

    @Test
    void add() {
        manufacturerController.add();
        assertTrue(manufacturerController.isAddingNew());
        assertNotNull(manufacturerController.getManufacturer());
    }

    @Test
    void save() {
        manufacturerController.save();
        Mockito.verify(manufacturerService).save(Mockito.any());
        Mockito.verify(manufacturerService).getAll();
    }

    @Test
    void cancel() {
        manufacturerController.setAddingNew(true);
        manufacturerController.setManufacturer(new Manufacturer());
        manufacturerController.cancel();
        assertFalse(manufacturerController.isAddingNew());
        assertNull(manufacturerController.getManufacturer());
    }
}