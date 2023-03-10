package com.inenergis.dao;

import com.inenergis.entity.CustomerNotificationPreference;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class CustomerNotificationPreferenceDao {


    @Inject
    EntityManager entityManager;

    public List<CustomerNotificationPreference> getBy(String saId, Date startDate, Date endDate) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(CustomerNotificationPreference.class);
        String quotedSaId = "'" + saId + "'"; //To avoid  warnings like: Cannot use ref access on index 'IDX_SA' due to type or collation conversion on field 'SA_ID'
        criteria.add(Restrictions.and(Restrictions.le("startDate", startDate), Restrictions.ge("endDate", endDate), Restrictions.eq("saId", quotedSaId)));

        return criteria.list();
    }

}
