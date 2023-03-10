package com.inenergis.entity.genericEnum;

public enum RelatedContractType {

    PURCHASE_POWER_AGREEMENT("Purchase Power Agreement"),
    DISTRIBUTION_CONTRACT("Energy Contract"),
    RATE_CONTRACT("Rate Contract");

    private String name;

    RelatedContractType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
