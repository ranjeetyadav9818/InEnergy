package com.inenergis.dao;

import com.inenergis.entity.locationRegistration.PmaxPmin;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class PmaxPminDao extends GenericDao<PmaxPmin> {
    public PmaxPminDao() {
        setClazz(PmaxPmin.class);
    }
}
