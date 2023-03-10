package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.workflow.TaskInstance;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyTaskInstanceDataModel extends LazyIdentifiableEntityDataModel<TaskInstance> {

    public LazyTaskInstanceDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, TaskInstance.class, preFilters);
    }
}