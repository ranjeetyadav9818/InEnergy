package com.inenergis.dao;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class BaselineAllowanceDao extends GenericDao<BaselineAllowance> {
    public BaselineAllowanceDao() {
        setClazz(BaselineAllowance.class);
    }

    public BaselineAllowance getByCalendarAndCode(TimeOfUseCalendar calendar, String code){
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());

        criteria.add(Restrictions.eq("timeOfUseCalendar.id", calendar.getId()));
        criteria.add(Restrictions.eq("code", code));

        return (BaselineAllowance) criteria.uniqueResult();
    }
}
