package com.inenergis.dao;

import com.inenergis.entity.PortalUser;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

/**
 * Created by egamas on 05/10/2017.
 */
@Stateless
@Transactional
public class PortalUserDao extends GenericDao<PortalUser>{
    public PortalUserDao() {
        setClazz(PortalUser.class);
    }
}
