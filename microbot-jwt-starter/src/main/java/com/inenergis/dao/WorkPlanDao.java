package com.inenergis.dao;

import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.workflow.WorkPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 04/10/2017.
 */
public interface WorkPlanDao extends Repository<WorkPlan,Long> {
    @Transactional("mysqlTransactionManager")
    @Modifying
    void save(WorkPlan workPlan);

    @Transactional("mysqlTransactionManager")
    List<WorkPlan> findAll();

    @Transactional("mysqlTransactionManager")
    WorkPlan getById(Long id);

    @Transactional("mysqlTransactionManager")
    WorkPlan getByProgramAndType(Program program, WorkPlanType workPlanType);


    @Transactional("mysqlTransactionManager")
    WorkPlan getByRatePlanAndType(RatePlan ratePlan, WorkPlanType workPlanType);
}
