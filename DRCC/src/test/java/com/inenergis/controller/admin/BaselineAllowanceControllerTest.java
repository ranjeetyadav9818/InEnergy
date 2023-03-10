package com.inenergis.controller.admin;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.service.BaselineAllowanceService;
import com.inenergis.service.TimeOfUseCalendarService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.primefaces.event.RowEditEvent;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaselineAllowanceControllerTest {

    @Mock
    private BaselineAllowanceService baselineAllowanceService;

    @Mock
    private TimeOfUseCalendarService timeOfUseCalendarService;

    @Mock
    private UIMessage uiMessage;

    @Mock
    private RowEditEvent rowEditEvent;

    @InjectMocks
    private BaselineAllowanceController baselineAllowanceController;

//
//    @InjectMocks
//    private BaselineThemsFactorController baseLineThemsFactorController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void add() {
        baselineAllowanceController.add();
        assertNotNull(baselineAllowanceController.getBaselineAllowance());
        assertTrue(baselineAllowanceController.isNewBaselineAllowance());
    }

    @Test
    void save() {
        baselineAllowanceController.save();
        Mockito.verify(baselineAllowanceService).saveOrUpdate(Mockito.any());
        assertNull(baselineAllowanceController.getBaselineAllowance());
        assertFalse(baselineAllowanceController.isNewBaselineAllowance());
    }

    @Test
    void cancelBaselineAllowance() {
        baselineAllowanceController.cancelBaselineAllowance();
        assertNull(baselineAllowanceController.getBaselineAllowance());
        assertFalse(baselineAllowanceController.isNewBaselineAllowance());
    }

    @Test
    void deleteBaselineAllowance() {
        BaselineAllowance baselineAllowance = new BaselineAllowance();
        baselineAllowance.setId(1L);

        baselineAllowanceController.setBaselineAllowanceList(new ArrayList<>());
        baselineAllowanceController.getBaselineAllowanceList().add(baselineAllowance);

        assertEquals(1, baselineAllowanceController.getBaselineAllowanceList().size());

        baselineAllowanceController.setSelectedBaselineAllowance(baselineAllowance);
        baselineAllowanceController.deleteBaselineAllowance();

        assertEquals(0, baselineAllowanceController.getBaselineAllowanceList().size());
    }

    @Test
    void onRowEdit() {
        Mockito.when(rowEditEvent.getObject()).thenReturn(new BaselineAllowance());
        baselineAllowanceController.onRowEdit(rowEditEvent);
        Mockito.verify(baselineAllowanceService).saveOrUpdate(Mockito.any());
    }
}