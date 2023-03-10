package com.inenergis.dao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class MeterDao {

    @Inject
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<String> getDistinctMtrConfigTypeValues() {
        String query = "SELECT DISTINCT configType FROM Meter WHERE configType IS NOT NULL";
        Query query1 = entityManager.createQuery(query);
        query1.setHint("org.hibernate.cacheable", true);
        return query1.getResultList();
    }
}
