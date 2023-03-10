package com.inenergis.entity.genericEnum;

public enum TierType {
    USAGE("Usage"),
    DEMAND("Demand"),
    VOLTAGE("Voltage"),
    PRESSURE("Pressure");

    private String name;

    TierType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}