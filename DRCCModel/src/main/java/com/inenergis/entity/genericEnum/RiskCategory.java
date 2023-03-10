package com.inenergis.entity.genericEnum;

public enum RiskCategory {
    CAPACITY("Capacity"),
    FATIGUE("Fatigue"),
    PROGRAM("Program");

    private String name;

    RiskCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}