package com.inenergis.billingEngine.service;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.billingEngine.sa.BaselineAllowanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BaselineAllowanceService {

    @Autowired
    private BaselineAllowanceDao baselineAllowanceDao;

    @Transactional("mysqlTransactionManager")
    public BaselineAllowance findByTimeOfUseCalendarAndCode(TimeOfUseCalendar calendar, String code) {
        return baselineAllowanceDao.findByTimeOfUseCalendarAndCode(calendar, code);
    }

}