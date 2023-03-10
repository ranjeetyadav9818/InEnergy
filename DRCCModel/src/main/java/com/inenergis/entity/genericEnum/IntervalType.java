package com.inenergis.entity.genericEnum;

public enum IntervalType {
    MINUTES("Minutes"),
    HOURS("Hours"),
    DAYS("Days"),
    WEEKS("Weeks");

    private String name;

    IntervalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}