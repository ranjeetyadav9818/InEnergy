package com.inenergis.dao;

import com.inenergis.entity.GasServicePoint;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Transactional
public class GasServicePointDao {

    @Inject
    EntityManager entityManager;

    public List<GasServicePoint> getAll() {
        return entityManager.createQuery("from ServicePoint", GasServicePoint.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<GasServicePoint> getByFeederId(String text) {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(GasServicePoint.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("feeder"), "feeder")))
                .setResultTransformer(Transformers.aliasToBean(GasServicePoint.class))
                .add(Restrictions.eq("feeder", text));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<String> getDistinctSubstations() {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(GasServicePoint.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("substation"), "substation")))
                .addOrder(Order.asc("substation"))
                .setResultTransformer(Transformers.aliasToBean(GasServicePoint.class));

        List<GasServicePoint> servicePoints = criteria.list();

        return servicePoints.stream()
                .map(GasServicePoint::getCityGate)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
//todogas    public List<String> getDistinctCircuits() {
//        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(GasServicePoint.class)
//                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("circuitNumber"), "circuitNumber")))
//                .addOrder(Order.asc("circuitNumber"))
//                .setResultTransformer(Transformers.aliasToBean(GasServicePoint.class));
//
//        List<GasServicePoint> servicePoints = criteria.list();
//
//        return servicePoints.stream()
//                .map(GasServicePoint::getCircuitNumber)
//                .collect(Collectors.toList());
//    }

    public GasServicePoint getById(String id) {
        return entityManager.find(GasServicePoint.class, id);
    }

    public List<GasServicePoint> getByIds(Collection<String> ids) {
        return entityManager.createQuery("from ServicePoint s WHERE s.servicePointId IN :ids").setParameter("ids", ids).getResultList();
    }

//    @SuppressWarnings("unchecked")
//    public List<GasServicePoint> getBySubstations(List<String> substations) {
//        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(GasServicePoint.class)
//                .add(Restrictions.in("substation", substations));
//
//        return criteria.list();
//  todogas  }

 //   @SuppressWarnings("unchecked")
//    public List<GasServicePoint> getByCircuits(List<String> circuits) {
//        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(GasServicePoint.class)
// todogas               .add(Restrictions.in("circuitNumber", circuits));
//
//        return criteria.list();
//    }
}