package com.inenergis.entity.genericEnum;

public enum TierDemandInterval {
    _5_MIN("5 min interval"),
    _15_MIN("15 min interval"),
    _1_HOUR("1 hour interval");

    private String name;

    TierDemandInterval(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}