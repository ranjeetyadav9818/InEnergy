package com.inenergis.service;

import com.inenergis.dao.TaskDao;
import com.inenergis.entity.workflow.Task;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Inject
    TaskDao taskDao;

    public void save(Task task) {
        taskDao.saveOrUpdate(task);
    }

    public List<Task> getAll() {
        return taskDao.getAll();
    }

    public Task getTaskById(Long id) {
        return taskDao.getById(id);
    }
}