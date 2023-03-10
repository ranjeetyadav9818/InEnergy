package com.inenergis.entity.genericEnum;

public enum RateConsumptionFeeType {
    DEBIT("Debit"),
    CREDIT("Credit");

    private String name;

    RateConsumptionFeeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}