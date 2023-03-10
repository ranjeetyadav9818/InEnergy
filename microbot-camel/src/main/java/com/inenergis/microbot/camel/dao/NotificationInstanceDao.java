package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.workflow.NotificationInstance;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NotificationInstanceDao extends Repository<NotificationInstance, Long> {
    List<NotificationInstance> findBySent(boolean sent);
}