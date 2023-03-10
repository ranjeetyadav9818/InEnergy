package com.inenergis.controller.program;

import com.inenergis.entity.genericEnum.RateCodeSector;
import com.inenergis.entity.genericEnum.RatePlanStatus;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.service.ChargesFeeService;
import com.inenergis.service.CreditDiscountFeeService;
import com.inenergis.service.PercentageFeeHierarchyService;
import com.inenergis.service.RatePlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RatePlanControllerTest {

    @Mock
    private RatePlanService ratePlanService;

    @Mock
    private ChargesFeeService chargesFeeService;

    @Mock
    private CreditDiscountFeeService creditDiscountFeeService;

    @Mock
    private PercentageFeeHierarchyService percentageFeeHierarchyService;

    @InjectMocks
    private RatePlanController controller = new RatePlanController();

    @BeforeEach
    void inject() {
        MockitoAnnotations.initMocks(this);
        RatePlan ratePlan = new RatePlan();
        controller.setRatePlan(ratePlan);
    }

    @Test
    void init() {
        controller.init();

        assertEquals(RatePlanStatus.ACTIVE, controller.getFilterRateCodeStatus());
        assertNotNull(controller.getLazyRatePlanDataModel());
    }

    @Test
    void testPreFilterGenerator() {
        controller.setFilterRateCodeStatus(RatePlanStatus.ACTIVE);
        controller.setFilterRateCodeSector(RateCodeSector.AGRICULTURAL);
        controller.setFilterRateCodeId("1");
        controller.search();

        assertTrue(controller.getLazyRatePlanDataModel().getPermanentFilters().containsKey("codeId"));
        assertTrue(controller.getLazyRatePlanDataModel().getPermanentFilters().containsKey("sector"));
        assertTrue(controller.getLazyRatePlanDataModel().getPermanentFilters().containsKey("status"));
    }

    @Test
    void save() {
        final RatePlan ratePlan = controller.getRatePlan();
        ratePlan.setProfiles(Arrays.asList(new RatePlanProfile()));
        ZonedDateTime start = ZonedDateTime.now().minusDays(10);
        ZonedDateTime end = ZonedDateTime.now().plusDays(10);

        ratePlan.getProfiles().get(0).setEffectiveStartDate(Date.from(start.toInstant()));
        ratePlan.getProfiles().get(0).setEffectiveEndDate(Date.from(end.toInstant()));
        assertNotNull(ratePlan.getActiveProfile());
        controller.save();
        Mockito.verify(ratePlanService).saveOrUpdate(ratePlan);
        assertNotNull(ratePlan.getActiveProfile().buildRateCodeId());
    }

    @Test
    void clear() {
        controller.setFilterRateCodeId("1");
        controller.setFilterRateCodeSector(RateCodeSector.AGRICULTURAL);
        controller.setFilterRateCodeStatus(RatePlanStatus.ACTIVE);
        controller.setEditMode(true);
        controller.clear();

        assertNull(controller.getFilterRateCodeId());
        assertNull(controller.getFilterRateCodeSector());
        assertEquals(RatePlanStatus.ACTIVE, controller.getFilterRateCodeStatus());
        assertFalse(controller.isEditMode());
    }

    @Test
    void add() {
        controller.setRatePlan(null);
        controller.setEditMode(false);
        controller.add();

        assertNotNull(controller.getRatePlan());
        assertTrue(controller.isEditMode());
    }

    @Test
    void update() {
        controller.setRatePlan(null);
        controller.update(new RatePlan());

        assertNotNull(controller.getRatePlan());
        assertTrue(controller.isEditMode());
    }
}