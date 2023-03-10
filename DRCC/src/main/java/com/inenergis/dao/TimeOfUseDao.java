package com.inenergis.dao;

import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Transactional
public class TimeOfUseDao extends GenericDao<TimeOfUse> {
    public TimeOfUseDao() {
        setClazz(TimeOfUse.class);
    }

    public List<TimeOfUse> getByCalendar(TimeOfUseCalendar calendar) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("timeOfUseCalendar.id").value(calendar.getId()).matchMode(MatchMode.EXACT).build());
        return getWithCriteria(conditions);
    }
}