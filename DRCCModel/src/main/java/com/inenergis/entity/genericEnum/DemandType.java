package com.inenergis.entity.genericEnum;

public enum DemandType {
    ALL_SEASONS_AND_PEAKS("All Seasons and peaks"),
    SUMMER_ON_PEAK("Summer on peak"),
    SUMMER_PARTIAL_PEAK("Summer partial peak"),
    WINTER_ON_PEAK("Winter on peak"),
    WINTER_PARTIAL_PEAK("Winter partial peak"),
    WINTER_OFF_PEAK("Winter off peak");

    private String name;

    DemandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}