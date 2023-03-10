package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.entity.workflow.TaskInstance;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskInstanceDao extends Repository<TaskInstance, Long> {

    List<TaskInstance> getAllByStatus(TaskStatus status);
}