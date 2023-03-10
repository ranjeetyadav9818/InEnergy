package com.inenergis.service;

import com.inenergis.dao.TimeOfUseCalendarDao;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Getter
@Setter
public class TimeOfUseCalendarService {

    @Inject
    TimeOfUseCalendarDao timeOfUseCalendarDao;

    public List<TimeOfUseCalendar> getAll() {
        return timeOfUseCalendarDao.getAll();
    }

    public TimeOfUseCalendar getById(Long key) {
        return timeOfUseCalendarDao.getById(key);
    }

    public TimeOfUseCalendar saveOrUpdate(TimeOfUseCalendar timeOfUseCalendar) {
        return timeOfUseCalendarDao.saveOrUpdate(timeOfUseCalendar);
    }
}
