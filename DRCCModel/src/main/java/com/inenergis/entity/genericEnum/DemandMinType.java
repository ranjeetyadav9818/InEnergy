package com.inenergis.entity.genericEnum;

public enum DemandMinType {
    ON_PEAK("On Peak"),
    OFF_PEAK("Off Peak"),
    PARTIAL_PEAK("Partial Peak");

    private String name;

    DemandMinType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}