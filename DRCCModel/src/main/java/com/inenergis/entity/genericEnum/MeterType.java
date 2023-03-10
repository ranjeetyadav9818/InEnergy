package com.inenergis.entity.genericEnum;

public enum MeterType {
    SMART_METER("Smart Meter"),
    MV90("MV90");

    private String name;

    MeterType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}