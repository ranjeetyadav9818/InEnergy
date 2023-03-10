package com.inenergis.entity.genericEnum;

public enum EnergyContractFeeType {
    ENERGY_PRICE("Energy Price"),
    BASE_FEE("Base Fee"),
    EMBEDDED_COSTS("Embedded Costs"),
    TRANSMISSION_AND_SYSTEM_COSTS("Transmission & System Costs"),
    ADMINISTRATIVE("Administrative"),
    MINIMUM_CHARGE("Minimum Charge");

    private String name;

    EnergyContractFeeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
