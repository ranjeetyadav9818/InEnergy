package com.inenergis.dao;

import com.inenergis.entity.workflow.TaskInstance;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class TaskInstanceDao extends GenericDao<TaskInstance>  {

    public TaskInstanceDao(){
        setClazz(TaskInstance.class);
    }

}
