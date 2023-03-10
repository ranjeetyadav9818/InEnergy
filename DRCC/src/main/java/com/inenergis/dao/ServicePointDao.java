package com.inenergis.dao;

import com.inenergis.entity.ServicePoint;
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
public class ServicePointDao {

    @Inject
    EntityManager entityManager;

    public List<ServicePoint> getAll() {
        return entityManager.createQuery("from ServicePoint", ServicePoint.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ServicePoint> getByFeederId(String text) {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ServicePoint.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("feeder"), "feeder")))
                .setResultTransformer(Transformers.aliasToBean(ServicePoint.class))
                .add(Restrictions.eq("feeder", text));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<String> getDistinctSubstations() {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ServicePoint.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("substation"), "substation")))
                .addOrder(Order.asc("substation"))
                .setResultTransformer(Transformers.aliasToBean(ServicePoint.class));

        List<ServicePoint> servicePoints = criteria.list();

        return servicePoints.stream()
                .map(ServicePoint::getSubstation)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<String> getDistinctCircuits() {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ServicePoint.class)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("circuitNumber"), "circuitNumber")))
                .addOrder(Order.asc("circuitNumber"))
                .setResultTransformer(Transformers.aliasToBean(ServicePoint.class));

        List<ServicePoint> servicePoints = criteria.list();

        return servicePoints.stream()
                .map(ServicePoint::getCircuitNumber)
                .collect(Collectors.toList());
    }

    public ServicePoint getById(String id) {
        return entityManager.find(ServicePoint.class, id);
    }

    public List<ServicePoint> getByIds(Collection<String> ids) {
        return entityManager.createQuery("from ServicePoint s WHERE s.servicePointId IN :ids").setParameter("ids", ids).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ServicePoint> getBySubstations(List<String> substations) {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ServicePoint.class)
                .add(Restrictions.in("substation", substations));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ServicePoint> getByCircuits(List<String> circuits) {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ServicePoint.class)
                .add(Restrictions.in("circuitNumber", circuits));

        return criteria.list();
    }
}