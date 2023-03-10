package com.inenergis.dao;

import com.inenergis.entity.program.CreditDiscountFee;
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
public class CreditDiscountFeeDao extends GenericDao<CreditDiscountFee> {

    @Inject
    EntityManager entityManager;

    public CreditDiscountFeeDao() {
        setClazz(CreditDiscountFee.class);
    }

    @SuppressWarnings("unchecked")
    public List<CreditDiscountFee> getAllComparisonEligible() {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(CreditDiscountFee.class);

        criteria.add(Restrictions.eq("comparisonEligible", true));

        return criteria.list();
    }
}
