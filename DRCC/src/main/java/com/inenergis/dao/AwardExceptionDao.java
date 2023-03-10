package com.inenergis.dao;

import com.inenergis.entity.award.AwardException;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AwardExceptionDao extends GenericDao<AwardException> {

    public AwardExceptionDao(){
        setClazz(AwardException.class);
    }

}
