package com.inenergis.dao;

import com.inenergis.entity.GasSecondaryContact;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class GasSecondaryContactDao extends GenericDao<GasSecondaryContact> {
    public GasSecondaryContactDao() {
        setClazz(GasSecondaryContact.class);
    }
}
