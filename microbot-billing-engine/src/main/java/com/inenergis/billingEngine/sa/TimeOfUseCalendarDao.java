package com.inenergis.billingEngine.sa;

import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeOfUseCalendarDao extends Repository<TimeOfUseCalendar, LocalDateTime> {

    List<TimeOfUseCalendar> findByDateFromIsLessThanEqualAndDateToIsGreaterThanEqual(LocalDate startDateTime, LocalDate endDateTime);

    TimeOfUseCalendar findById(Long id);
}
