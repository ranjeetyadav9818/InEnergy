package com.inenergis.entity.genericEnum;

public enum ComodityType {

    Electricity("Electricity"),
//    WATER("Water"),
    Gas("Gas");

    private String name;

    ComodityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
