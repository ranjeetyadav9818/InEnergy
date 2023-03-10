package com.inenergis.entity.genericEnum;


public enum CreditDiscountFrequency {

    FIRST_TIME_BILL("First Time Bill"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly");

    private String name;

    CreditDiscountFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
