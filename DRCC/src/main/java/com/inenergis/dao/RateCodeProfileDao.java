package com.inenergis.dao;

import com.inenergis.entity.program.rateProgram.RateCodeProfile;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class RateCodeProfileDao extends GenericDao<RateCodeProfile>{
    public RateCodeProfileDao(){ setClazz(RateCodeProfile.class);}
}
