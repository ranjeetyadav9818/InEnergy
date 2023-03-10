package com.inenergis.dao;

import com.inenergis.entity.workflow.NotificationDefinition;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class NotificationDefinitionDao extends GenericDao<NotificationDefinition>  {

    public NotificationDefinitionDao(){
        setClazz(NotificationDefinition.class);
    }

}
