package com.inenergis.entity.genericEnum;

public enum PaymentFrequency {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    ANNUALLY("Annually");

    private String name;

    PaymentFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}