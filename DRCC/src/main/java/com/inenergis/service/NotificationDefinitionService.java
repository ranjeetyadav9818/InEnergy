package com.inenergis.service;

import com.inenergis.dao.NotificationDefinitionDao;
import com.inenergis.entity.workflow.NotificationDefinition;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class NotificationDefinitionService {

    private static final Logger log = LoggerFactory.getLogger(NotificationDefinitionService.class);

    @Inject
    NotificationDefinitionDao notificationDefinitionDao;

    public void save(NotificationDefinition notificationDefinition) {
        notificationDefinitionDao.saveOrUpdate(notificationDefinition);
    }

    public List<NotificationDefinition> getAll() {
        return notificationDefinitionDao.getAll();
    }

    public NotificationDefinition getById(Long id) {
        return notificationDefinitionDao.getById(id);
    }
}