package com.inenergis.service;

import com.inenergis.dao.PortalUserDao;
import com.inenergis.entity.PortalUser;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by egamas on 05/10/2017.
 */
@Stateless
@Getter
@Setter
public class PortalUserService {
    @Inject
    PortalUserDao portalUserDao;

    public PortalUser saveOrUpdate(PortalUser user) {
        user.setPassword(user.getPasswordNew());
        return portalUserDao.saveOrUpdate(user);
    }

    public void delete(PortalUser user) {
        portalUserDao.delete(user);
    }
}
