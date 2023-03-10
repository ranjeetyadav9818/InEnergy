package com.inenergis.dao;

import com.inenergis.entity.program.ChargesFee;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
@Getter
@Setter
public class ChargesFeeDao extends GenericDao<ChargesFee> {

    @Inject
    EntityManager entityManager;

    public ChargesFeeDao() {
        setClazz(ChargesFee.class);
    }

    @SuppressWarnings("unchecked")
    public List<ChargesFee> getAllComparisonEligible() {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(ChargesFee.class);

        criteria.add(Restrictions.eq("comparisonEligible", true));

        return criteria.list();
    }
}
