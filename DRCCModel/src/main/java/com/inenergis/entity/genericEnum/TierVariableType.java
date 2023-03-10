package com.inenergis.entity.genericEnum;

public enum TierVariableType {
    BASELINE_ALLOWANCE("Customer Baseline Allowance (kWh)");

    private String name;

    TierVariableType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}