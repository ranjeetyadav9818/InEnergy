package com.inenergis.util;

import com.inenergis.entity.genericEnum.MinutesOrHours;
import com.inenergis.entity.genericEnum.MinutesOrHoursOrDays;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class TimeUtil {

    public static final Long SECONDS_IN_A_DAY = 60L * 60L * 24L;

    public static Long convertToSeconds(Integer value, MinutesOrHoursOrDays unit) {
        long resourceCurtailmentTimeInMinutes;
        switch (unit) {
            case DAYS:
                resourceCurtailmentTimeInMinutes = value * 24 * 60;
                break;
            case HOURS:
                resourceCurtailmentTimeInMinutes = value * 60;
                break;
            default:
                resourceCurtailmentTimeInMinutes = value;
                break;
        }
        return resourceCurtailmentTimeInMinutes;
    }

    public static Long convertToSeconds(Integer value, MinutesOrHours unit) {
        long resourceCurtailmentTimeInMinutes;
        switch (unit) {
            case HOURS:
                resourceCurtailmentTimeInMinutes = value * 60;
                break;
            default:
                resourceCurtailmentTimeInMinutes = value;
                break;
        }
        return resourceCurtailmentTimeInMinutes;
    }

    public static Date adaptTimeToCustomer(Date date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
    }

    public static String getTradeDateFormatted(Date tradeDate) {
        if (tradeDate != null) {
            return ConstantsProviderModel.DATE_FORMAT.format(tradeDate);
        }
        return StringUtils.EMPTY;
    }

    public static Date getStartOfDay(Date date) {
        return getStartOfDay(date, ZoneId.systemDefault());
    }

    public static Date getEndOfDay(Date date) {
        return getEndOfDay(date, ZoneId.systemDefault());
    }

    public static Date getStartOfDay(Date date, ZoneId zoneId) {
        LocalDateTime dateAtStartOfDay = LocalDate.from(date.toInstant().atZone(zoneId))
                .atStartOfDay();

        return Date.from(dateAtStartOfDay.atZone(zoneId).toInstant());
    }

    public static Date getEndOfDay(Date date, ZoneId zoneId) {
        LocalDateTime dateAtEndOfDay = LocalDate.from(date.toInstant().atZone(zoneId))
                .atStartOfDay()
                .plus(1, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.SECONDS);

        return Date.from(dateAtEndOfDay.atZone(zoneId).toInstant());
    }

    public static boolean between(Date startDate, Date endDate, Date... datesToCheck) {
        Instant startInstant = startDate.toInstant();
        Instant endInstant = endDate.toInstant();
        for (Date date : datesToCheck) {
            if (date.toInstant().isBefore(startInstant) || date.toInstant().isAfter(endInstant)) {
                return false;
            }
        }
        return true;
    }

    public static Date customerNow() {
        return Date.from(LocalDate.now().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
    }

    public static Long calculateTotalElapsedTime(Date start, Date end, Long elapsedPausedTime) {
        if (start == null || end == null) {
            return null;
        }
        Long pausedTime = elapsedPausedTime == null ? 0 : elapsedPausedTime;
        long seconds = Duration.between(start.toInstant(), end.toInstant().minusSeconds(pausedTime)).getSeconds();
        return seconds;
    }
}
