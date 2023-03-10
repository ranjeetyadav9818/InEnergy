package com.inenergis.entity.genericEnum;

public enum GasTierVariableType {
    BASELINE_ALLOWANCE("Customer Baseline Allowance (Therms)");

    private String name;

    GasTierVariableType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}