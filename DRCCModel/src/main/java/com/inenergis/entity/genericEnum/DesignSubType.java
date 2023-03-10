package com.inenergis.entity.genericEnum;

public enum DesignSubType {
    ENERGY("Energy"),
    CARE("CARE");

    private String name;

    DesignSubType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}