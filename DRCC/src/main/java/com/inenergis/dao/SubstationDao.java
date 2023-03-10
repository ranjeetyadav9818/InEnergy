package com.inenergis.dao;

import com.inenergis.entity.Substation;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class SubstationDao extends GenericDao<Substation>  {

    public SubstationDao(){
        setClazz(Substation.class);
    }

}
