package com.inenergis.entity.genericEnum;

public enum GasRateAncillaryFrequency {
    THERMS_DAY("Therms/day"),
    THERMS_INVOICE("Therms/invoice"),
    INVOICE("Invoice"),
    METER_DAY("per meter/day"),
    ONE_TIME("One time"),
    PER_DAY("per day");

    private String name;

    GasRateAncillaryFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}