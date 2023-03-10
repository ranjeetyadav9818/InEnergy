package com.inenergis.billingEngine.service;

import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import com.inenergis.entity.genericEnum.RateEventFee;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import com.inenergis.billingEngine.sa.RateProfileConsumptionFeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RateProfileConsumptionFeeService {

    @Autowired
    private RateProfileConsumptionFeeDao rateProfileConsumptionFeeDao;

    @Transactional("mysqlTransactionManager")
    public List<RateProfileConsumptionFee> findBy(RateConsumptionFeeType rateType, TimeOfUseCalendar calendar, TimeOfUse timeOfUse, RateEventFee eventFee) {
        return rateProfileConsumptionFeeDao.findAllBy(rateType, calendar, timeOfUse, eventFee);
    }
}
