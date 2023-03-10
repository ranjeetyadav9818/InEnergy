package com.inenergis.dao;

import com.inenergis.entity.marketIntegration.CommoditySubProgram;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class CommoditySubProgramDao extends GenericDao<CommoditySubProgram> {

    public CommoditySubProgramDao() {
        setClazz(CommoditySubProgram.class);
    }
}