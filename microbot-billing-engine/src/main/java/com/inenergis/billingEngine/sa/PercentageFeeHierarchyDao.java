package com.inenergis.billingEngine.sa;

import com.inenergis.entity.program.rateProgram.PercentageFeeHierarchyEntry;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PercentageFeeHierarchyDao extends Repository<PercentageFeeHierarchyEntry, String> {

    List<PercentageFeeHierarchyEntry> findByName(String name);
}
