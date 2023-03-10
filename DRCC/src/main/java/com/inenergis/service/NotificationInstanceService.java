package com.inenergis.service;

import com.inenergis.dao.NotificationInstanceDao;
import com.inenergis.entity.workflow.NotificationInstance;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class NotificationInstanceService {

    private static final Logger log = LoggerFactory.getLogger(NotificationInstanceService.class);

    @Inject
    NotificationInstanceDao notificationInstanceDao;

    public void save(NotificationInstance notificationInstance) {
        notificationInstanceDao.saveOrUpdate(notificationInstance);
    }

    public List<NotificationInstance> getAll() {
        return notificationInstanceDao.getAll();
    }

    public NotificationInstance getById(Long id) {
        return notificationInstanceDao.getById(id);
    }

    public void saveAll(List<NotificationInstance> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (NotificationInstance notificationInstance : list) {
                notificationInstanceDao.save(notificationInstance);
            }
        }
    }
}