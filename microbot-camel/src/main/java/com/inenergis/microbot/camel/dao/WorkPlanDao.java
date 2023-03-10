package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.workflow.WorkPlan;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface WorkPlanDao extends Repository<WorkPlan, Long> {

    WorkPlan save(WorkPlan workPlan);

    WorkPlan findByProgramAndType(Program program, WorkPlanType workPlanType);
}