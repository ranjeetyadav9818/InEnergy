package com.inenergis.entity.genericEnum;

public enum AmountType {
    FLAT_FEE("Flat Fee"),
    PERCENT_OF_INDEX("Percent of Index"),
    FIXED_PRICE("Fixed price ($/MWH)");

    private String name;

    AmountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
