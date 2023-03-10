package com.inenergis.entity.genericEnum;

public enum RateEventFee {
    PDP("PDP"),
    SMARTRATE("Smart rate");

    private String name;

    RateEventFee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}