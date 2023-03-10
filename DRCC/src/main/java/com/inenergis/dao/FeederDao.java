package com.inenergis.dao;

import com.inenergis.entity.Feeder;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class FeederDao extends GenericDao<Feeder>  {

    public FeederDao(){
        setClazz(Feeder.class);
    }

}
