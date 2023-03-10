package com.inenergis.entity.genericEnum;

public enum CommodityFrequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly");

    private String name;

    CommodityFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
