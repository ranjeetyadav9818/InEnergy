package com.inenergis.dao;

import com.inenergis.entity.program.RatePlan;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class RatePlanDao extends GenericDao<RatePlan> {
    public RatePlanDao() {
        setClazz(RatePlan.class);
    }
}
