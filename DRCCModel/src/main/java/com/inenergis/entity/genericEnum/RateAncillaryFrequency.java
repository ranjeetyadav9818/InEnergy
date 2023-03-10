package com.inenergis.entity.genericEnum;

public enum RateAncillaryFrequency {
    KW_DAY("kW/day"),
    KW_INVOICE("kW/invoice"),
    INVOICE("Invoice"),
    METER_DAY("per meter/day"),
    ONE_TIME("One time"),
    PER_DAY("per day");

    private String name;

    RateAncillaryFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}