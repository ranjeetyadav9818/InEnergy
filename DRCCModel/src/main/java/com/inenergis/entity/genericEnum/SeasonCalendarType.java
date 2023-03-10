package com.inenergis.entity.genericEnum;

public enum SeasonCalendarType {
    RATE_PROGRAM_CALENDAR("Rate Program Calendar"),
    PROGRAM_CALENDAR("Program Calendar");

    private String name;

    SeasonCalendarType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
