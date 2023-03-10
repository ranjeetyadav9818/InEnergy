package com.inenergis.entity.genericEnum;

public enum TierBoundType {
    AMOUNT("Amount"),
    FORMULA("Formula"),
    PREVIOUS_BAND("Previous band"),
    UNLIMITED("Unlimited");

    private String name;

    TierBoundType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}