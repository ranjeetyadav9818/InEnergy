package com.inenergis.entity.genericEnum;

public enum EnergyContractCreditType {
    GOVERNMENT_CREDIT("Government Credit"),
    EMBEDDED_BENEFITS("Embedded Benefits"),
    XYZ_CREDIT("XYZ Credit");

    private String name;

    EnergyContractCreditType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
