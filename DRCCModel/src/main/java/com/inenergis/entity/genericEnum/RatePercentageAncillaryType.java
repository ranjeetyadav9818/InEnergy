package com.inenergis.entity.genericEnum;

public enum RatePercentageAncillaryType {
    MANUAL("Manual"),
    DYNAMIC("Dynamic");

    private String name;

    RatePercentageAncillaryType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}