package com.inenergis.billingEngine.sa;

import com.inenergis.entity.Event;
import org.springframework.data.repository.Repository;

import java.util.Date;

public interface EventDao extends Repository<Event, Date> {

    Event findByStartDateIsLessThanAndEndDateIsGreaterThanEqual(Date startDateTime, Date endDateTime);
}
