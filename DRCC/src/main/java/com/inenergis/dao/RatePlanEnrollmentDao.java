package com.inenergis.dao;

import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class RatePlanEnrollmentDao extends GenericDao<RatePlanEnrollment> {
    public RatePlanEnrollmentDao() {
        setClazz(RatePlanEnrollment.class);
    }
}
