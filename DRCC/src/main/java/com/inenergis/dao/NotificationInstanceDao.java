package com.inenergis.dao;

import com.inenergis.entity.workflow.NotificationInstance;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class NotificationInstanceDao extends GenericDao<NotificationInstance>  {

    public NotificationInstanceDao(){
        setClazz(NotificationInstance.class);
    }

}
