package com.inenergis.dao;

import com.inenergis.entity.marketIntegration.CommodityProgram;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class CommodityProgramDao extends GenericDao<CommodityProgram> {

    public CommodityProgramDao() {
        setClazz(CommodityProgram.class);
    }
}
