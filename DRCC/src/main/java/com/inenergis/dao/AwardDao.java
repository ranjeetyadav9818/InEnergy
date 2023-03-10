package com.inenergis.dao;

import com.inenergis.entity.award.Award;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AwardDao extends GenericDao<Award>{
    public AwardDao(){setClazz(Award.class);}
}
