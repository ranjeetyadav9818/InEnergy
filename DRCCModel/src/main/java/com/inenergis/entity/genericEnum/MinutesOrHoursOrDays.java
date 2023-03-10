package com.inenergis.entity.genericEnum;

import java.time.temporal.ChronoUnit;

public enum MinutesOrHoursOrDays {
    MINUTES("Minutes", ChronoUnit.MINUTES),
    HOURS("Hours", ChronoUnit.HOURS),
    DAYS("Days", ChronoUnit.DAYS);

    private String name;
    private ChronoUnit chronoUnit;

    MinutesOrHoursOrDays(String name, ChronoUnit chronoUnit) {
        this.name = name;
        this.chronoUnit = chronoUnit;
    }

    public String getName() {
        return name;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }
}