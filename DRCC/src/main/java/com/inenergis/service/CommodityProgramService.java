package com.inenergis.service;

import com.inenergis.dao.CommodityProgramDao;
import com.inenergis.entity.marketIntegration.CommodityProgram;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CommodityProgramService {

    @Inject
    CommodityProgramDao commodityProgramDao;

    public List<CommodityProgram> getAll() {
        return commodityProgramDao.getAll();
    }

    public CommodityProgram getById(Long id) {
        return commodityProgramDao.getById(id);
    }
}