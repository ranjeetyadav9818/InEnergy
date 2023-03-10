package com.inenergis.service;

import com.inenergis.dao.CommoditySubProgramDao;
import com.inenergis.entity.marketIntegration.CommoditySubProgram;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CommoditySubProgramService {

    @Inject
    CommoditySubProgramDao commoditySubProgramDao;

    public List<CommoditySubProgram> getAll() {
        return commoditySubProgramDao.getAll();
    }

    public CommoditySubProgram getById(Long id) {
        return commoditySubProgramDao.getById(id);
    }
}