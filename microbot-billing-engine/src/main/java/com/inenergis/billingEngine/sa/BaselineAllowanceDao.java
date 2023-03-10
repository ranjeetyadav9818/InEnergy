package com.inenergis.billingEngine.sa;

import com.inenergis.entity.BaselineAllowance;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface  BaselineAllowanceDao extends Repository<BaselineAllowance, Long> {

    BaselineAllowance findByTimeOfUseCalendarAndCode(TimeOfUseCalendar calendar, String code);

}
