package com.inenergis.service;

import com.inenergis.dao.TimeOfUseDao;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Getter
@Setter
public class TimeOfUseService {

    @Inject
    TimeOfUseDao timeOfUseDao;

    public List<TimeOfUse> getAll() {
        return timeOfUseDao.getAll();
    }

    public TimeOfUse getById(Long key) {
        return timeOfUseDao.getById(key);
    }

    public TimeOfUse saveOrUpdate(TimeOfUse timeOfUse) {
        return timeOfUseDao.saveOrUpdate(timeOfUse);
    }

    public List<TimeOfUse> getByCalendar(TimeOfUseCalendar calendar){
        return timeOfUseDao.getByCalendar(calendar);
    }
}