package com.inenergis.dao;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Stateless
@Transactional
public class ServiceAgreementDao {

    //TODO generic one

    @Inject
    EntityManager entityManager;

    public List<ServiceAgreement> getAll() {
        return entityManager.createQuery("from ServiceAgreement").getResultList();
    }

    public BaseServiceAgreement getById(String id) {
        return entityManager.find(BaseServiceAgreement.class, id);
    }



    public List<ServiceAgreement> getAllByIds(Collection<String> ids) {
        return entityManager.createQuery("from ServiceAgreement s WHERE s.serviceAgreementId IN :ids").setParameter("ids", ids).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<String> getDistinctHas3rdPartyValues() {
        String query = "SELECT DISTINCT has3rdPartyDrp FROM ServiceAgreement WHERE has3rdPartyDrp IS NOT NULL";
        Query query1 = entityManager.createQuery(query);
        query1.setHint("org.hibernate.cacheable", true);
        return query1.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<String> getDistinctCustClassCdValues() {
        String query = "SELECT DISTINCT custClassCd FROM ServiceAgreement WHERE custClassCd IS NOT NULL";
        Query query1 = entityManager.createQuery(query);
        query1.setHint("org.hibernate.cacheable", true);
        return query1.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<String> getDistinctCustSizeValues() {
        String query = "SELECT DISTINCT cust_size FROM ServiceAgreement WHERE cust_size IS NOT NULL";
        Query query1 = entityManager.createQuery(query);
        query1.setHint("org.hibernate.cacheable", true);
        return query1.getResultList();
    }

}
