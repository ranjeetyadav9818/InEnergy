package com.inenergis.entity.genericEnum;

public enum MeterIntervalType {
    HOUR_1("1 Hour"),
    MINUTES_15("15 Minutes"),
    MINUTES_5("5 Minutes");

    private String name;

    MeterIntervalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}