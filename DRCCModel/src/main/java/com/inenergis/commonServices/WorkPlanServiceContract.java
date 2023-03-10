package com.inenergis.commonServices;

import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.exception.BusinessException;

import java.util.List;

/**
 * Created by egamas on 15/10/2017.
 */
public interface WorkPlanServiceContract {
    void save(WorkPlan workPlan);

    List<WorkPlan> getAll();

    WorkPlan getById(Long id);

    WorkPlan getByProgramAndWorkPlanType(Program program, WorkPlanType workPlanType);

    WorkPlan getByRatePlan(RatePlan ratePlan, WorkPlanType workPlanType);

    void savePlanInstance(PlanInstance planInstance);

    void createProgramEnrollmentPlan(ProgramServiceAgreementEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException;

    void createRatePlanEnrollmentPlan(RatePlanEnrollment enrollmen, JMSUtilContract jmsUtilt) throws BusinessException;

    void createProgramUnenrollmentPlan(ProgramServiceAgreementEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException;

    void createRatePlanUnenrollmentPlan(RatePlanEnrollment enrollment, JMSUtilContract jmsUtil) throws BusinessException;
}
