package com.inenergis.dao;

import com.inenergis.entity.program.rateProgram.PercentageFeeHierarchyEntry;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class PercentageFeeHierarchyDao extends GenericDao<PercentageFeeHierarchyEntry> {
    public PercentageFeeHierarchyDao() {
        setClazz(PercentageFeeHierarchyEntry.class);
    }

    public List<String> getDistinctPercentageFeeHierarchyNames() {
        String query = "SELECT DISTINCT (name) FROM PercentageFeeHierarchyEntry";
        Query query1 = entityManager.createQuery(query);
        query1.setHint("org.hibernate.cacheable", true);
        return query1.getResultList();
    }
}
