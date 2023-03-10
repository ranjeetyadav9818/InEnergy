package com.inenergis.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class PremiseDao {

    @Inject
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    public List<String> getDistinctPremiseTypeValues() {
        String query = "SELECT DISTINCT premiseType FROM Premise WHERE premiseType IS NOT NULL";
        Query query1 = entityManager.createQuery(query);
        query1.setHint("org.hibernate.cacheable", true);
        return query1.getResultList();
    }
}
