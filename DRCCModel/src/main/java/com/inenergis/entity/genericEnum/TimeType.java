package com.inenergis.entity.genericEnum;

public enum TimeType {
    PEAK("Peak"),
    PART_PEAK("Part-Peak"),
    OFF_PEAK("Off-Peak");

    private String name;

    TimeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}