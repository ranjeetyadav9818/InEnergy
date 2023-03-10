package com.inenergis.entity.genericEnum;


public enum DispatchForecastFormat {
    DAILY_DETAIL("Daily Detail"),
    SUMMARIZED("Summarized");

    DispatchForecastFormat(String text) {
        this.name = text;
    }

    private String name;

    public String getName() {
        return name;
    }
}
