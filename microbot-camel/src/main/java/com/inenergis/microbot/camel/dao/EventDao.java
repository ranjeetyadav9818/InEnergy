package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.Event;
import com.inenergis.entity.program.Program;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public interface EventDao extends Repository<Event, Long> {

    List<Event> findByEventNotificationEventState(String state);

    List<Event> findByProgramInAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Collection<Program> programs, Date startDate, Date endDate);
}