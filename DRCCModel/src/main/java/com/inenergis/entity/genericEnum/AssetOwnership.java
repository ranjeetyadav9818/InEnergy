package com.inenergis.entity.genericEnum;

public enum AssetOwnership {
    UTILITY("Utility"),
    CUSTOMER("Customer"),
    MERCHANT("Merchant");

    private String name;

    AssetOwnership(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}