package com.inenergis.dao;

import com.inenergis.entity.program.ProgramAggregator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class ProgramAggregatorDao extends GenericDao<ProgramAggregator> {

    @Inject
    EntityManager entityManager;

    public ProgramAggregatorDao() {
        setClazz(ProgramAggregator.class);
    }

    @SuppressWarnings("unchecked")
    public List<ProgramAggregator> getAllActive() {
        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ProgramAggregator.class);

        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        criteria.add(Restrictions.or(
                Restrictions.isNull("effectiveStartDate"),
                Restrictions.le("effectiveStartDate", currentDate)));
        criteria.add(Restrictions.or(
                Restrictions.isNull("effectiveEndDate"),
                Restrictions.ge("effectiveEndDate", currentDate)));

        return criteria.list();
    }
}
