package com.inenergis.service;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.dao.PlanInstanceDao;
import com.inenergis.dao.WorkPlanDao;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/**
 * Created by egamas on 15/10/2017.
 */
@Component
public class WorkPlanService implements WorkPlanServiceContract {

    private WorkflowEngine workflowEngine = new WorkflowEngine();

    @Autowired
    private WorkPlanDao workPlanDao;

    @Autowired
    private PlanInstanceDao planInstanceDao;

    @Autowired
    @Qualifier("appProperties")
    private Properties properties;


    @Override
    @Transactional("mysqlTransactionManager")
    public void save(WorkPlan workPlan) {
        workPlanDao.save(workPlan);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<WorkPlan> getAll() {
        return workPlanDao.findAll();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public WorkPlan getById(Long id) {
        return workPlanDao.getById(id);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public WorkPlan getByProgramAndWorkPlanType(Program program, WorkPlanType workPlanType) {
       return workPlanDao.getByProgramAndType(program, workPlanType);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public WorkPlan getByRatePlan(RatePlan ratePlan, WorkPlanType workPlanType) {
        return workPlanDao.getByRatePlanAndType(ratePlan, workPlanType);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void savePlanInstance(PlanInstance planInstance) {
        planInstanceDao.save(planInstance);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public void createProgramEnrollmentPlan(ProgramServiceAgreementEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewProgramPlan(enrollment, WorkPlanType.ENROL, this, properties, jmsUtil);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public void createRatePlanEnrollmentPlan(RatePlanEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewRatePlan(enrollment, WorkPlanType.RATE_PLAN_ENROLLMENT, this, properties, jmsUtil);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public void createProgramUnenrollmentPlan(ProgramServiceAgreementEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewProgramPlan(enrollment, WorkPlanType.UNENR, this, properties, jmsUtil);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public void createRatePlanUnenrollmentPlan(RatePlanEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException {
        workflowEngine.triggerNewRatePlan(enrollment, WorkPlanType.RATE_PLAN_UNENROLLMENT, this, properties , jmsUtil);
    }
}
