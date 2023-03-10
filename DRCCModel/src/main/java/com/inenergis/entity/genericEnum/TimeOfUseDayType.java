package com.inenergis.entity.genericEnum;

public enum TimeOfUseDayType {
    WEEK_DAYS("Week Days"),
    WEEK_ENDS("Week Ends");

    private String name;

    TimeOfUseDayType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}