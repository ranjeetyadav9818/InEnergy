package com.inenergis.dao;

import com.inenergis.entity.marketIntegration.IsoProfile;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class IsoProfileDao extends GenericDao<IsoProfile> {

    public IsoProfileDao() {
        setClazz(IsoProfile.class);
    }
}