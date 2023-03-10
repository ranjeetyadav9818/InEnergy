package com.inenergis.entity.genericEnum;

public enum RateAncillaryFeeType {
    FLAT("Flat"),
    PERCENTAGE_OVER_TOTAL("% over total");

    private String name;

    RateAncillaryFeeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}