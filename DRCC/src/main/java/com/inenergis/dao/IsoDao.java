package com.inenergis.dao;

import com.inenergis.entity.marketIntegration.Iso;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class IsoDao extends GenericDao<Iso> {
    public IsoDao() {
        setClazz(Iso.class);
    }
}
