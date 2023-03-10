package com.inenergis.dao;

import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class TimeOfUseCalendarDao extends GenericDao<TimeOfUseCalendar> {
    public TimeOfUseCalendarDao() {
        setClazz(TimeOfUseCalendar.class);
    }
}