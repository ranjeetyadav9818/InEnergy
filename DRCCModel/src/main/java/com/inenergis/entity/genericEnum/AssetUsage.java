package com.inenergis.entity.genericEnum;

public enum AssetUsage {
    CONSUMPTION("Consumption"),
    PRODUCTION("Production"),
    BOTH("Both");

    private String name;

    AssetUsage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}