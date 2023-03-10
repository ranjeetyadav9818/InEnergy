package com.inenergis.entity.genericEnum;

import java.time.temporal.ChronoUnit;

public enum MinutesOrHours {
    MINUTES("Minutes", ChronoUnit.MINUTES),
    HOURS("Hours", ChronoUnit.HOURS);

    private String name;
    private ChronoUnit chronoUnit;

    MinutesOrHours(String name, ChronoUnit chronoUnit) {
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