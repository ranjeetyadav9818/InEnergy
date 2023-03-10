package com.inenergis.dao;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.util.CacheAll;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.isNotNull;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.not;


public abstract class GenericDao<T extends IdentifiableEntity> {

    private Class<T> clazz;

    @Inject
    EntityManager entityManager;

    @Transactional
    public T save(T object) {
        entityManager.persist(object);
        return object;
    }

    public List<T> getAll() {
        Query query = entityManager.createQuery("from " + clazz.getName());
        if (clazz.isAnnotationPresent(CacheAll.class)) {
            query.setHint("org.hibernate.cacheable", true);
        }
        return query.getResultList();
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getById(Long id) {
        return entityManager.find(clazz, id);
    }

    @Transactional
    public T saveOrUpdate(T object) {
        if (object.getId() != null) {
            return entityManager.merge(object);
        } else {
            return save(object);
        }
    }

    @Transactional
    public void delete(T object) {
        if (object.getId() != null) {
            entityManager.remove(getById(object.getId()));
        }
    }

    public List<T> getWithCriteria(List<CriteriaCondition> conditions, Integer limit, String orderField, Boolean descending) {
        Order order = null;
        if (orderField != null && descending != null) {
            if (descending) {
                order = Order.desc(orderField);
            } else {
                order = Order.asc(orderField);
            }
        }
        Criteria criteria = generateCriteria(conditions, order);
        if (limit != null) {
            criteria.setMaxResults(limit);
        }
        return criteria.list();
    }


    public List<T> getWithCriteria(List<CriteriaCondition> conditions, Integer limit) {
        return getWithCriteria(conditions, limit, null, null);
    }

    public List<T> getWithCriteria(List<CriteriaCondition> conditions) {
        return getWithCriteria(conditions, null);
    }

    public T getUniqueResultWithCriteria(List<CriteriaCondition> conditions) {
        Criteria criteria = generateCriteria(conditions, null);
        return (T) criteria.uniqueResult();
    }

    public Long countWithCriteria(List<CriteriaCondition> conditions) {
        Criteria criteria = generateCriteria(conditions, null);
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count;
    }

    private Criteria generateCriteria(List<CriteriaCondition> conditions, Order order) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(clazz);
        Set<String> aliases = new HashSet<>();
        for (CriteriaCondition condition : conditions) {
            if (condition.getNoComparisonCheck() != null) {
                switch (condition.getNoComparisonCheck()) {
                    case NULL:
                        criteria.add(isNull(addAlias(criteria, condition.getKey(), aliases)));
                        break;
                    case NOT_NULL:
                        criteria.add(isNotNull(addAlias(criteria, condition.getKey(), aliases)));
                        break;
                }
            } else {
                Criterion criterion;
                switch (condition.getMatchMode()) {
                    case EXACT:
                        if (Collection.class.isAssignableFrom(condition.getValue().getClass())) {
                            criterion = in(addAlias(criteria, condition.getKey(), aliases), (Collection) condition.getValue());
                        } else {
                            criterion = eq(addAlias(criteria, condition.getKey(), aliases), condition.getValue());
                        }
                        break;
                    default:
                        criterion = like(addAlias(criteria, condition.getKey(), aliases), condition.getValue().toString(), condition.getMatchMode());
                }
                if (condition.isNegate()) {
                    criteria.add(not(criterion));
                } else {
                    criteria.add(criterion);
                }
            }
        }
        addOrder(criteria, order);
        return criteria;
    }

    private void addOrder(Criteria criteria, Order order) {
        if (order != null) {
            criteria.addOrder(order);
        }
    }

    public static String addAlias(Criteria criteria, String key, Set<String> aliases) {
        final String[] split = key.split("\\.");
        StringBuilder aliasSoFar = new StringBuilder();
        String lastAlias = "";
        for (int i = 0; i < split.length - 1; i++) {
            if (!aliases.contains(split[i])) {
                criteria.createAlias(lastAlias + split[i], split[i] + 1);
                aliases.add(split[i]);
            }
            lastAlias = split[i] + 1 + ".";
            aliasSoFar.append(split[i]).append(".");
        }
        key = key.replace(aliasSoFar.toString(), lastAlias);
        return key;
    }

    public List<T> getByIds(Collection<Long> ids) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(clazz);
        criteria.add(in("id", ids));
        return (List<T>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAllByType(Class<T> clazz) {
        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(clazz);
        return criteria.list();
    }
}
