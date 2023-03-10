package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.workflow.NotificationInstance;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyNotificationInstanceDataModel extends LazyIdentifiableEntityDataModel<NotificationInstance> {

    public LazyNotificationInstanceDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, NotificationInstance.class, preFilters);
    }
}