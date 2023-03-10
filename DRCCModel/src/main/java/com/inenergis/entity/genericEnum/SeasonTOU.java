package com.inenergis.entity.genericEnum;

public enum SeasonTOU {
    ON_PEAK("On-peak"),
    OFF_PEAK("Off-peak"),
    PARTIAL_PEAK("Partial peak");

    private String name;

    SeasonTOU(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
