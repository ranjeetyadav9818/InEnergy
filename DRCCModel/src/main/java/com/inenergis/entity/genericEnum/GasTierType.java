package com.inenergis.entity.genericEnum;

public enum GasTierType {
    USAGE("Usage"),
    DEMAND("Demand"),
    PRESSURE("Pressure");

    private String name;

    GasTierType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}