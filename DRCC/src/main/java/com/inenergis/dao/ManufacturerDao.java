package com.inenergis.dao;

import com.inenergis.entity.Manufacturer;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ManufacturerDao extends GenericDao<Manufacturer> {
    public ManufacturerDao() {
        setClazz(Manufacturer.class);
    }
}
