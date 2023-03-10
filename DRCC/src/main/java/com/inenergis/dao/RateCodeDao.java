package com.inenergis.dao;

import com.inenergis.entity.program.rateProgram.RateCode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class RateCodeDao extends GenericDao<RateCode>{
    public RateCodeDao(){setClazz(RateCode.class);}
}
