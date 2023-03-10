package com.inenergis.entity.genericEnum;


public enum EnergyContractType {

    PURCHASE_POWER_AGREEMENT("Purchase Power Agreement"),
    DISTRIBUTION_CONTRACT("Distribution Contract"),
    RATE_CONTRACT("Rate Contract");

    private String name;

    EnergyContractType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
