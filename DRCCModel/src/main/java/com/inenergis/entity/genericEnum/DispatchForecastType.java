package com.inenergis.entity.genericEnum;


public enum DispatchForecastType {

    RAW_FORECAST("Raw Forecast"),
    ADJUSTED_FORECAST("Adjusted Forecast");

    private String name;

    public String getName() {
        return name;
    }

    DispatchForecastType(String text) {

        this.name = text;
    }
}
