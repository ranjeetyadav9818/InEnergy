package com.inenergis.service;

import com.inenergis.dao.BaselineThermsDao;
import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.BaselineTherms;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BaselineThermsService {

    @Inject
    private BaselineThermsDao baselineThermsDao;

    public void delete(BaselineTherms baselineTherms) {
        baselineThermsDao.delete(baselineTherms);
    }

    public List<BaselineTherms> getAll() {
        return baselineThermsDao.getAll();
    }

    public BaselineTherms getById(Long id) {
        return baselineThermsDao.getById(id);
    }

    public void saveOrUpdate(BaselineTherms baselineTherms) {
        baselineThermsDao.saveOrUpdate(baselineTherms);
    }

    public BaselineTherms getByCalendarAndCode(TimeOfUseCalendar calendar, String code){
        return baselineThermsDao.getByCalendarAndCode(calendar,code);
    }
}