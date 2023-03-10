package com.inenergis.service;

import com.inenergis.dao.BaselineAllowanceDao;
import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BaselineAllowanceService {

    @Inject
    private BaselineAllowanceDao baselineAllowanceDao;

    public void delete(BaselineAllowance baselineAllowance) {
        baselineAllowanceDao.delete(baselineAllowance);
    }

    public List<BaselineAllowance> getAll() {
        return baselineAllowanceDao.getAll();
    }

    public BaselineAllowance getById(Long id) {
        return baselineAllowanceDao.getById(id);
    }

    public void saveOrUpdate(BaselineAllowance baselineAllowance) {
        baselineAllowanceDao.saveOrUpdate(baselineAllowance);
    }

    public BaselineAllowance getByCalendarAndCode(TimeOfUseCalendar calendar, String code){
        return baselineAllowanceDao.getByCalendarAndCode(calendar,code);
    }
}