package com.inenergis.entity.genericEnum;

public enum EventDurationOption {
    ENTIRE_PROGRAM("Entire Program"),
    DAY_AHEAD("Day-Ahead"),
    DAY_OFF("Day-Off");

    private String name;

    EventDurationOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}