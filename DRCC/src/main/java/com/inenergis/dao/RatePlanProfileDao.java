package com.inenergis.dao;

import com.inenergis.entity.program.RatePlanProfile;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class RatePlanProfileDao extends GenericDao<RatePlanProfile> {
    public RatePlanProfileDao() {
        setClazz(RatePlanProfile.class);
    }
}
