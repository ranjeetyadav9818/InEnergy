package com.inenergis.util;

import com.inenergis.entity.program.EnrollmentAttribute;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
public final class ConstantsProviderModel {


    public static final String TRUE = "true";

    private ConstantsProviderModel() {
    }

    public static final long KW_PRECISION = 1_000L;
    public static final long MW_PRECISION = 1_000_000L;
    public static final BigDecimal ONE_HUNDRED_BIG_DECIMAL = new BigDecimal("100.0");
    public static final BigDecimal ONE_THOUSAND_BIG_DECIMAL = new BigDecimal("1000.0");
    public static final BigDecimal ONE_MILLION_BIG_DECIMAL = new BigDecimal("1000000.0");
    public static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    public static final ZoneId CUSTOMER_TIMEZONE_ID = ZoneId.of("America/Los_Angeles");
    public static final long DEFAULT_EXPIRING_YEARS_CAISO_LOCATIONS = 10L;
    public static final long DEFAULT_EXPIRING_YEARS_CAISO_REGISTRATION = 1L;

    public static final List<DayOfWeek> WEEKDAYS = new ArrayList<>(
            Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));

    public static int LOCATION_CHANGELOG_DAYS_IN_ADVANCE_ALLOWED = 1;

    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(MM_DD_YYYY);
    public static final Locale LOCALE = new Locale("en", "US");
    public static final EnrollmentAttribute[] ENROLLMENT_ATTRIBUTES = EnrollmentAttribute.values();


}
