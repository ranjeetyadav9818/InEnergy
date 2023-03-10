package com.inenergis.dao;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.like;

@Stateless
@Transactional
public class AgreementPointMapDao {

    //TODO generic one

    @Inject
    EntityManager entityManager;

    public List<AgreementPointMap> getAll(){
        return entityManager.createQuery("from AgreementPointMap").getResultList();
    }

    public AgreementPointMap getById(String id) {
        return entityManager.find(AgreementPointMap.class,id);
    }

    public List<AgreementPointMap> getAllByIds(Collection<String> ids){
        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(AgreementPointMap.class);
        criteria.createAlias("serviceAgreement","sa");
        criteria.add(in("sa.serviceAgreementId",ids));
        return criteria.list();
    }

}
