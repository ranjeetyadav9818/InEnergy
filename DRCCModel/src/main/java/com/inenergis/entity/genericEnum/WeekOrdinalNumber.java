package com.inenergis.entity.genericEnum;

public enum WeekOrdinalNumber {
    FIRST("First"),
    SECOND("Second"),
    THIRD("Third"),
    FOURTH("Fourth"),
    LAST("Last");

    private String name;

    WeekOrdinalNumber(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}