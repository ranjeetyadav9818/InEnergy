package com.inenergis.billingEngine.service;

import com.inenergis.entity.program.rateProgram.PercentageFeeHierarchyEntry;
import com.inenergis.billingEngine.sa.PercentageFeeHierarchyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PercentageFeeHierarchyService {

    @Autowired
    PercentageFeeHierarchyDao percentageFeeHierarchyDao;

    @Transactional("mysqlTransactionManager")
    public List<PercentageFeeHierarchyEntry> getByName(String name) {
        return percentageFeeHierarchyDao.findByName(name);
    }
}