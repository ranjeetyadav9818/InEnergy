package com.inenergis.entity.genericEnum;


public enum DiscountCategory {

    CLIMATE("Climate"),
    BASELINE("Baseline"),
    GENERATOR_STANDBY_SERVICE("Generator Standby Service"),
    HIGH_VOLTAGE_DISTRIBUTION("High Voltage Distribution"),
    JOINT_EAP_MEDICAL_EQUIPMENT("Joint EAP & Medical Equipment"),
    HYDROGENERATION("Hydrogeneration"),
    LOW_INCOME("Low Income"),
    MEDICAL_EQUIPMENT("Medical Equipment"),
    NON_PROFIT("Non-Profit"),
    PEAK_DAY_PRICING("Peak Day Pricing"),
    STATE("State"),
    TBD_CLIENT_SPECIFIC("TBD Client Specific");

    private String name;

    DiscountCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
