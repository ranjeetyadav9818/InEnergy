package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.entity.workflow.NotificationDefinition;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NotificationDefinitionDao extends Repository<NotificationDefinition, Long> {
    List<NotificationDefinition> findAll();

    NotificationDefinition getByType(NotificationDefinitionId type);
}