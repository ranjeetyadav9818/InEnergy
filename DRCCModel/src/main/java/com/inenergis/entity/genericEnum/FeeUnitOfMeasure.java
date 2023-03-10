package com.inenergis.entity.genericEnum;

public enum FeeUnitOfMeasure {
    PER_MONTH("Per Month"),
    PER_PREMISE("Per Premise"),
    PER_METER("Per Meter"),
    PER_XYZ("Per XYZ");

    private String name;

    FeeUnitOfMeasure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
