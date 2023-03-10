package com.inenergis.entity.genericEnum;

public enum ChargesFeeComparator {
    HIGHER("Higher"),
    LOWER("Lower");

    private String name;

    ChargesFeeComparator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
