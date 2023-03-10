package com.inenergis.entity.genericEnum;

public enum RiskSource {
    FORECASTED_FSL("Forecasted-FSL"),
    P_MIN("P Min"),
    P_MAX("P Max"),
    CONSECUTIVE_DISPATCH_DAYS_THRESHOLD("Location Ratio under Consecutive Dispatch Days Threshold"),
    MAX_HOURS_ACHIEVED("Location Ratio under Max Hours Threshold");

    private String name;

    RiskSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}