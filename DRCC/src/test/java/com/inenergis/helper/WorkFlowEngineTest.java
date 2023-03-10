package com.inenergis.helper;


import com.inenergis.entity.genericEnum.IntervalType;
import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.entity.genericEnum.TaskType;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.ProgramPlanInstance;
import com.inenergis.entity.workflow.RatePlanInstance;
import com.inenergis.entity.workflow.Task;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.entity.workflow.WorkPlanTaskList;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.util.JMSUtil;
import com.inenergis.util.VelocityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class WorkFlowEngineTest {

    ProgramServiceAgreementEnrollment programServiceAgreementEnrollment;

    RatePlanEnrollment ratePlanEnrollment;

    WorkPlan workPlan;

    WorkPlanTaskList workPlanTaskList1;
    WorkPlanTaskList workPlanTaskList2;
    WorkPlanTaskList workPlanTaskList3;

    @Mock
    VelocityUtil velocityUtil;

    @Mock
    Properties properties;

    @Mock
    JMSUtil jmsUtil;

    @BeforeEach
    void inject() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    void initPojos() {
        programServiceAgreementEnrollment = new ProgramServiceAgreementEnrollment();
        ratePlanEnrollment = new RatePlanEnrollment();
        workPlan = new WorkPlan();

        workPlanTaskList1 = new WorkPlanTaskList();
        workPlanTaskList1.setOrder("1.1");
        final Task task = new Task();
        task.setDurationMeasure(IntervalType.DAYS);
        task.setDurationValue(1);
        task.setTaskType(TaskType.USER);

        workPlanTaskList1.setTask(task);

        workPlanTaskList2 = new WorkPlanTaskList();
        workPlanTaskList2.setOrder("1.2");
        workPlanTaskList2.setTask(task);

        workPlanTaskList3 = new WorkPlanTaskList();
        workPlanTaskList3.setOrder("2.2");
        workPlanTaskList3.setTask(task);

        workPlan.setWorkPlanTaskLists(Arrays.asList(workPlanTaskList1, workPlanTaskList2, workPlanTaskList3));

    }

    @Test
    void buildProgramPlanInstance() {

        final PlanInstance planInstance = new WorkflowEngine().buildProgramPlanInstance(programServiceAgreementEnrollment, workPlan, velocityUtil, properties, jmsUtil);
        Assertions.assertNotNull(planInstance.getWorkPlan());
        Assertions.assertNotNull(planInstance.getStart());
        Assertions.assertNotNull(planInstance.getStatus());
        Assertions.assertNotNull(planInstance.getStatus());

        Assertions.assertEquals(planInstance.getClass(), ProgramPlanInstance.class);
        ProgramPlanInstance programPlanInstance = (ProgramPlanInstance) planInstance;
        Assertions.assertNotNull(programPlanInstance.getProgramServiceAgreementEnrollment());

        final Long howManyPendingAction = planInstance.getTaskInstances().stream().filter(i -> i.getStatus().equals(TaskStatus.PENDING_ACTION)).collect(Collectors.counting());

        Assertions.assertEquals(1L,howManyPendingAction.longValue());

        final Long howManyInProcess = planInstance.getTaskInstances().stream().filter(i -> i.getStatus().equals(TaskStatus.IN_PROCESS)).collect(Collectors.counting());
        Assertions.assertEquals(2L,howManyInProcess.longValue());

    }

    @Test
    void buildRateProgramPlanInstance() {

        final PlanInstance planInstance = new WorkflowEngine().buildRatePlanInstance(ratePlanEnrollment, workPlan, velocityUtil, properties, jmsUtil);
        Assertions.assertNotNull(planInstance.getWorkPlan());
        Assertions.assertNotNull(planInstance.getStart());
        Assertions.assertNotNull(planInstance.getStatus());
        Assertions.assertNotNull(planInstance.getStatus());

        Assertions.assertEquals(planInstance.getClass(), RatePlanInstance.class);
        RatePlanInstance ratePlanInstance = (RatePlanInstance) planInstance;
        Assertions.assertNotNull(ratePlanInstance.getRatePlanEnrollment());

        final Long howManyPendingAction = planInstance.getTaskInstances().stream().filter(i -> i.getStatus().equals(TaskStatus.PENDING_ACTION)).collect(Collectors.counting());

        Assertions.assertEquals(1L,howManyPendingAction.longValue());

        final Long howManyInProcess = planInstance.getTaskInstances().stream().filter(i -> i.getStatus().equals(TaskStatus.IN_PROCESS)).collect(Collectors.counting());
        Assertions.assertEquals(2L,howManyInProcess.longValue());
    }


}
