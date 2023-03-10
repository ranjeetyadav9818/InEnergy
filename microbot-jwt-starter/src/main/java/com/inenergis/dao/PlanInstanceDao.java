package com.inenergis.dao;

import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.WorkPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egamas on 04/10/2017.
 */
public interface PlanInstanceDao extends Repository<PlanInstance,Long> {

    @Transactional("mysqlTransactionManager")
    @Modifying
    void save(PlanInstance planInstance);


}
