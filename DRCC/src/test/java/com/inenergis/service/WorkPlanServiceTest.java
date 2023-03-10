package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.PlanInstanceDao;
import com.inenergis.dao.WorkPlanDao;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.ProgramPlanInstance;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.exception.BusinessException;
import com.inenergis.util.JMSUtil;
import com.inenergis.util.PropertyAccessor;
import org.hibernate.criterion.MatchMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class WorkPlanServiceTest {

    @Mock
    private WorkPlanDao workPlanDao;

    @Mock
    private PropertyAccessor propertyAccessor;

    @Mock
    private WorkflowEngine workflowEngine;

    @Mock
    private PlanInstanceDao planInstanceDao;

    @Mock
    private JMSUtil jmsUtil;

    @Captor
    private ArgumentCaptor<List<CriteriaCondition>> conditionsCaptor;

    @InjectMocks
    private WorkPlanService workPlanService;

    private ProgramServiceAgreementEnrollment programServiceAgreementEnrollment;

    private RatePlanEnrollment ratePlanEnrollment;

    @BeforeEach
    void inject() {
        MockitoAnnotations.initMocks(this);
        PlanInstance planInstance = new ProgramPlanInstance();
        Mockito.when(workflowEngine.buildProgramPlanInstance(any(), any(), any(), any(),any())).thenReturn(planInstance);
        Mockito.when(propertyAccessor.getProperties()).thenReturn(new Properties());

        programServiceAgreementEnrollment = new ProgramServiceAgreementEnrollment();
        programServiceAgreementEnrollment.setProgram(new Program());

        ratePlanEnrollment = new RatePlanEnrollment();
        ratePlanEnrollment.setRatePlan(new RatePlan());
        workflowEngine = new WorkflowEngine();
    }

    @Test
    void getByRatePlan() {
        RatePlan ratePlan = new RatePlan();
        final WorkPlanType workPlanType = WorkPlanType.RATE_PLAN_ENROLLMENT;
        workPlanService.getByRatePlan(ratePlan, workPlanType);
        final CriteriaCondition criteriaCondition1 = CriteriaCondition.builder().key("ratePlan").value(ratePlan).matchMode(MatchMode.EXACT).build();
        final CriteriaCondition criteriaCondition2 = CriteriaCondition.builder().key("type").value(workPlanType).matchMode(MatchMode.EXACT).build();
        Mockito.verify(workPlanDao).getUniqueResultWithCriteria(conditionsCaptor.capture());
        for (CriteriaCondition criteriaCondition : conditionsCaptor.getValue()) {
//            Assertions.assertTrue(criteriaCondition1.equals(criteriaCondition)
//                    || criteriaCondition2.equals(criteriaCondition));
        }
    }

    @Test
    void getByProgramAndWorkPlanType() {
        final Program program = new Program();
        final WorkPlanType workPlanType = WorkPlanType.ENROL;
        workPlanService.getByProgramAndWorkPlanType(program, workPlanType);
        final CriteriaCondition criteriaCondition1 = CriteriaCondition.builder().key("program").value(program).matchMode(MatchMode.EXACT).build();
        final CriteriaCondition criteriaCondition2 = CriteriaCondition.builder().key("type").value(workPlanType).matchMode(MatchMode.EXACT).build();
        Mockito.verify(workPlanDao).getUniqueResultWithCriteria(conditionsCaptor.capture());
        for (CriteriaCondition criteriaCondition : conditionsCaptor.getValue()) {
            Assertions.assertTrue(criteriaCondition1.equals(criteriaCondition)
                    || criteriaCondition2.equals(criteriaCondition));
        }
    }

    @Test
    void save() {
        WorkPlan workPlan = new WorkPlan();
        workPlanService.save(workPlan);
        Mockito.verify(workPlanDao).saveOrUpdate(workPlan);
    }

    @Test
    void getAll() {
        workPlanService.getAll();
        Mockito.verify(workPlanDao).getAll();
    }

    @Test
    void getById() {
        workPlanService.getById(1L);
        Mockito.verify(workPlanDao).getById(any());
    }

    @Test
    void createProgramEnrollmentPlan() throws BusinessException {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(Mockito.anyList())).thenReturn(new WorkPlan());
        workPlanService.createProgramEnrollmentPlan(programServiceAgreementEnrollment, jmsUtil);
    }

    @Test
    void createProgramEnrollmentPlan_ThrowsException() {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(Mockito.anyList())).thenReturn(null);
        assertThrows(BusinessException.class, () -> workflowEngine.triggerNewProgramPlan(programServiceAgreementEnrollment, WorkPlanType.ENROL, workPlanService, propertyAccessor.getProperties(),jmsUtil));
    }

    @Test
    void createRatePlanEnrollmentPlan() throws BusinessException {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(any())).thenReturn(new WorkPlan());
        workPlanService.createRatePlanEnrollmentPlan(ratePlanEnrollment, jmsUtil);
    }

    @Test
    void createRatePlanEnrollmentPlan_ThrowsException() {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(any())).thenReturn(null);
        assertThrows(BusinessException.class, () -> workflowEngine.triggerNewRatePlan(ratePlanEnrollment, WorkPlanType.RATE_PLAN_ENROLLMENT, workPlanService, propertyAccessor.getProperties(),jmsUtil));
    }

    @Test
    void createProgramUnenrollmentPlan() throws BusinessException {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(any())).thenReturn(new WorkPlan());
        workPlanService.createProgramUnenrollmentPlan(programServiceAgreementEnrollment, jmsUtil);
    }

    @Test
    void createProgramUnenrollmentPlan_ThrowsException() {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(any())).thenReturn(null);
        assertThrows(BusinessException.class, () -> workflowEngine.triggerNewProgramPlan(programServiceAgreementEnrollment, WorkPlanType.UNENR, workPlanService, propertyAccessor.getProperties(),jmsUtil));
    }

    @Test
    void createRatePlanUnenrollmentPlan() throws BusinessException {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(any())).thenReturn(new WorkPlan());
        workPlanService.createRatePlanUnenrollmentPlan(ratePlanEnrollment, jmsUtil);
    }

    @Test
    void createRatePlanUnenrollmentPlan_ThrowsException() {
        Mockito.when(workPlanDao.getUniqueResultWithCriteria(any())).thenReturn(null);
        assertThrows(BusinessException.class, () ->
                workflowEngine.triggerNewRatePlan(ratePlanEnrollment, WorkPlanType.RATE_PLAN_UNENROLLMENT, workPlanService, propertyAccessor.getProperties(),jmsUtil));
    }
}
