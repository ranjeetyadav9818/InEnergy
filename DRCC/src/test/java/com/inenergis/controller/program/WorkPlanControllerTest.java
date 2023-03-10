package com.inenergis.controller.program;

import com.inenergis.controller.workflow.WorkPlanController;
import com.inenergis.entity.genericEnum.ProgramType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.service.ProgramService;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.TaskService;
import com.inenergis.service.WorkPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WorkPlanControllerTest {

    @Mock
    private WorkPlanService workPlanService;

    @Mock
    private RatePlanService ratePlanService;

    @Mock
    private ProgramService programService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private WorkPlanController controller;

    @BeforeEach
    void inject(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void initProgram() {
        controller.init();
        assertEquals(ProgramType.DEMAND_RESPONSE, controller.getProgramType() );
        assertNotNull(controller.getWorkPlanTypes());
        assertNotNull(controller.getProgramList());
    }

    @Test
    void initRatePlan() {
        Mockito.when(ratePlanService.getById(Mockito.anyLong())).thenReturn(new RatePlan());
        controller.init();
        controller.setProgramType(ProgramType.RATE);
        assertNotNull(controller.getWorkPlanTypes());
        assertNotNull(controller.getRatePlanList());
    }

    @Test
    void searchProgram(){
        controller.init();
        controller.setProgram(new Program());
        controller.search();
        assertNotNull(controller.getWorkPlan());
        assertNotNull(controller.getWorkPlan().getProgram());
    }
}
