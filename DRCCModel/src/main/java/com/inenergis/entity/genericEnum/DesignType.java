package com.inenergis.entity.genericEnum;

public enum DesignType {
    TIERED("Tiered"),
    TIME_OF_USE("Time of Use");

    private String name;

    DesignType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}