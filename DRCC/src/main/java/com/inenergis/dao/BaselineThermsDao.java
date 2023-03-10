package com.inenergis.dao;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.BaselineTherms;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class BaselineThermsDao extends GenericDao<BaselineTherms> {
    public BaselineThermsDao() {
        setClazz(BaselineTherms.class);
    }

    public BaselineTherms getByCalendarAndCode(TimeOfUseCalendar calendar, String code){
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());

        criteria.add(Restrictions.eq("timeOfUseCalendar.id", calendar.getId()));
        criteria.add(Restrictions.eq("code", code));

        return (BaselineTherms) criteria.uniqueResult();
    }
}