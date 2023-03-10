package com.inenergis.dao;

import com.inenergis.entity.workflow.Task;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class TaskDao extends GenericDao<Task>  {

    public TaskDao(){
        setClazz(Task.class);
    }

}
