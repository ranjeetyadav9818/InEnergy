package com.inenergis.dao;

import com.inenergis.entity.workflow.WorkPlanTaskList;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class WorkPlanTaskListDao extends GenericDao<WorkPlanTaskList>  {

    public WorkPlanTaskListDao(){
        setClazz(WorkPlanTaskList.class);
    }

}
