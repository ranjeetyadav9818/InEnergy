package com.inenergis.dao;

import com.inenergis.entity.SecondaryContact;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class SecondaryContactDao extends GenericDao<SecondaryContact> {
    public SecondaryContactDao() {
        setClazz(SecondaryContact.class);
    }
}
