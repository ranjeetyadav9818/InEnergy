package com.inenergis.service;

import com.inenergis.dao.PercentageFeeHierarchyDao;
import com.inenergis.entity.program.rateProgram.PercentageFeeHierarchyEntry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PercentageFeeHierarchyService {

    @Inject
    private PercentageFeeHierarchyDao percentageFeeHierarchyDao;

    public void delete(PercentageFeeHierarchyEntry fee) {
        percentageFeeHierarchyDao.delete(fee);
    }

    public List<PercentageFeeHierarchyEntry> getAll() {
        return percentageFeeHierarchyDao.getAll();
    }

    public void saveOrUpdate(PercentageFeeHierarchyEntry fee) {
        percentageFeeHierarchyDao.saveOrUpdate(fee);
    }

    public List<String> getDistinctPercentageFeeHierarchyNames() {
        return percentageFeeHierarchyDao.getDistinctPercentageFeeHierarchyNames();
    }

}