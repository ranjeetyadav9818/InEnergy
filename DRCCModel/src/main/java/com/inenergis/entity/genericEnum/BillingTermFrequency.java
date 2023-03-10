package com.inenergis.entity.genericEnum;

public enum BillingTermFrequency {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    ANNUALLY("Annually");

    private String name;

    BillingTermFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}