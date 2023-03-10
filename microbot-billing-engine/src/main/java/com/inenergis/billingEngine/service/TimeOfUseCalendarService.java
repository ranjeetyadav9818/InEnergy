package com.inenergis.billingEngine.service;

import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.masterCalendar.TimeOfUseDay;
import com.inenergis.entity.masterCalendar.TimeOfUseHour;
import com.inenergis.billingEngine.sa.TimeOfUseCalendarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TimeOfUseCalendarService {

    private static final List<DayOfWeek> weekdays = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
    private static final List<DayOfWeek> weekends = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    @Autowired
    private TimeOfUseCalendarDao timeOfUseCalendarDao;

    @Transactional("mysqlTransactionManager")
    @Cacheable("calendars-by-date")
    public List<TimeOfUseCalendar> findByDate(LocalDate localDate) {
        List<TimeOfUseCalendar> calendars = timeOfUseCalendarDao.findByDateFromIsLessThanEqualAndDateToIsGreaterThanEqual(localDate, localDate);
        calendars.forEach(calendar -> calendar.getTimeOfUses().forEach(tou -> {tou.getTimeOfUseDays().size();tou.getTimeOfUseHours().size();}));
        return calendars;
    }

    @Transactional("mysqlTransactionManager")
//    @Cacheable("tous-by-calendar-and-time")
    public TimeOfUse getTimeOfUse(TimeOfUseCalendar calendar, LocalDateTime meterReadingDateTime) {

        if (calendar == null) {
            return null;
        }
        TimeOfUse timeOfUse = null;

        for (TimeOfUse tou : calendar.getTimeOfUses()) {
            boolean hoursMatched = false;
            boolean dayMatched = false;
            for (TimeOfUseHour timeOfUseHour : tou.getTimeOfUseHours()) {
                if (timeOfUseHour.getHour()-1 == meterReadingDateTime.getHour()) {
                    hoursMatched = true;
                    break;
                }
            }
            for (TimeOfUseDay timeOfUseDay : tou.getTimeOfUseDays()) {
                switch (timeOfUseDay.getDay()) {
                    case WEEK_DAYS:
                        if (weekdays.contains(meterReadingDateTime.getDayOfWeek())) {
                            dayMatched = true;
                        }
                        break;
                    case WEEK_ENDS:
                        if (weekends.contains(meterReadingDateTime.getDayOfWeek())) {
                            dayMatched = true;
                        }
                        break;
                }
            }

            if (hoursMatched && dayMatched) {
                timeOfUse = tou;
                break;
            }
        }

        return timeOfUse;
    }
}
