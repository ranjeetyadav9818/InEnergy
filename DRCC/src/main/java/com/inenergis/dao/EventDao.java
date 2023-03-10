package com.inenergis.dao;

import com.inenergis.entity.Event;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.program.Program;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class EventDao extends GenericDao<Event> {

    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    public EventDao() {
        setClazz(Event.class);
    }

    @SuppressWarnings("unchecked")
    public List<Event> getBy(Program program, Date startDate, Date endDate, List<EventStatus> statuses) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());

        Criterion rest1 = Restrictions.and(Restrictions.ge(START_DATE, startDate), Restrictions.le(START_DATE, endDate));
        Criterion rest2 = Restrictions.and(Restrictions.ge(END_DATE, startDate), Restrictions.le(END_DATE, endDate));
        Criterion rest3 = Restrictions.eq("program.id", program.getId());

        criteria.add(Restrictions.or(rest1, rest2))
                .add(rest3);

        if (!statuses.isEmpty()) {
            criteria.add(Restrictions.in("status", statuses));
        }

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Event> getAllForPeriod(List<Program> programs, Date startDate, Date endDate) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());

        criteria.add(Restrictions.in("program", programs))
                .add(Restrictions.ge(START_DATE, startDate))
                .add(Restrictions.le(END_DATE, endDate));

        return criteria.list();
    }
}
