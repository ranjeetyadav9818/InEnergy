package com.inenergis.service;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.PlanInstanceDao;
import com.inenergis.dao.WorkPlanDao;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.exception.BusinessException;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.VelocityUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Stateless
public class WorkPlanService implements WorkPlanServiceContract {

    private static final Logger log = LoggerFactory.getLogger(WorkPlanService.class);

    private WorkflowEngine workflowEngine = new WorkflowEngine();

    @Inject
    private WorkPlanDao workPlanDao;

    @Inject
    private PlanInstanceDao planInstanceDao;

    @Inject
    private PropertyAccessor propertyAccessor;

    @Override
    public void save(WorkPlan workPlan) {
        workPlanDao.saveOrUpdate(workPlan);
    }

    @Override
    public List<WorkPlan> getAll() {
        return workPlanDao.getAll();
    }

    @Override
    public WorkPlan getById(Long id) {
        return workPlanDao.getById(id);
    }

    @Override
    public WorkPlan getByProgramAndWorkPlanType(Program program, WorkPlanType workPlanType) {

        CriteriaCondition programCondition = CriteriaCondition.builder().key("program").value(program).matchMode(MatchMode.EXACT).build();
        CriteriaCondition typeCondition = CriteriaCondition.builder().key("type").value(workPlanType).matchMode(MatchMode.EXACT).build();
        return workPlanDao.getUniqueResultWithCriteria(Arrays.asList(programCondition, typeCondition));
    }

    @Override
    public WorkPlan getByRatePlan(RatePlan ratePlan, WorkPlanType workPlanType) {
        CriteriaCondition ratePlanCondition = CriteriaCondition.builder().key("ratePlan.id").value(ratePlan.getId()).matchMode(MatchMode.EXACT).build();
        CriteriaCondition typeCondition = CriteriaCondition.builder().key("type").value(workPlanType).matchMode(MatchMode.EXACT).build();
        return workPlanDao.getUniqueResultWithCriteria(Arrays.asList(ratePlanCondition, typeCondition));
    }

    @Override
    public void savePlanInstance(PlanInstance planInstance) {
        planInstanceDao.saveOrUpdate(planInstance);
    }

    @Override
    public void createProgramEnrollmentPlan(ProgramServiceAgreementEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewProgramPlan(enrollment, WorkPlanType.ENROL, this, propertyAccessor.getProperties(),jmsUtil);
    }

    @Override
    public void createRatePlanEnrollmentPlan(RatePlanEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewRatePlan(enrollment, WorkPlanType.RATE_PLAN_ENROLLMENT, this, propertyAccessor.getProperties(),jmsUtil);
    }

    @Override
    public void createProgramUnenrollmentPlan(ProgramServiceAgreementEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewProgramPlan(enrollment, WorkPlanType.UNENR, this, propertyAccessor.getProperties(),jmsUtil);
    }

    @Override
    public void createRatePlanUnenrollmentPlan(RatePlanEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewRatePlan(enrollment, WorkPlanType.RATE_PLAN_UNENROLLMENT, this, propertyAccessor.getProperties(),jmsUtil);
    }


}