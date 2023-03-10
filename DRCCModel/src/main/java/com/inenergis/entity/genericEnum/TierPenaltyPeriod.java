package com.inenergis.entity.genericEnum;

public enum TierPenaltyPeriod {
    PER_DAY("Per day"),
    PER_INVOICE("Per invoice");

    private String name;

    TierPenaltyPeriod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}