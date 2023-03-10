package com.inenergis.service;

import com.inenergis.dao.WorkPlanTaskListDao;
import com.inenergis.entity.workflow.WorkPlanTaskList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class WorkPlanTaskListService {

    private static final Logger log = LoggerFactory.getLogger(WorkPlanTaskListService.class);

    @Inject
    WorkPlanTaskListDao workPlanTaskListDao;

    public void save(WorkPlanTaskList workPlanTaskList) {
        workPlanTaskListDao.saveOrUpdate(workPlanTaskList);
    }

    public List<WorkPlanTaskList> getAll() {
        return workPlanTaskListDao.getAll();
    }

    public WorkPlanTaskList getById(Long id) {
        return workPlanTaskListDao.getById(id);
    }

}