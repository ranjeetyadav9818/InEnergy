package com.inenergis.service;

import com.inenergis.dao.TaskInstanceDao;
import com.inenergis.entity.workflow.TaskInstance;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class TaskInstanceService {

    private static final Logger log = LoggerFactory.getLogger(TaskInstanceService.class);

    @Inject
    TaskInstanceDao taskInstanceDao;

    public void save(TaskInstance taskInstance) {
        taskInstanceDao.saveOrUpdate(taskInstance);
    }

    public List<TaskInstance> getAll() {
        return taskInstanceDao.getAll();
    }

    public TaskInstance getById(Long id) {
        return taskInstanceDao.getById(id);
    }
}