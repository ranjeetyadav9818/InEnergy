package com.inenergis.dao;

import com.inenergis.entity.workflow.WorkPlan;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class WorkPlanDao extends GenericDao<WorkPlan>  {

    public WorkPlanDao(){
        setClazz(WorkPlan.class);
    }
}
