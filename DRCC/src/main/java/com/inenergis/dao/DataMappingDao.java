package com.inenergis.dao;

import com.inenergis.entity.DataMapping;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class DataMappingDao extends GenericDao<DataMapping> {
    public DataMappingDao() {
        setClazz(DataMapping.class);
    }
}
