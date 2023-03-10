package com.inenergis.entity.genericEnum;

public enum CommodityType {

    ELECTRICITY("Electricity"),
//    WATER("Water"),
    GAS("Gas");

    private String name;

    CommodityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
