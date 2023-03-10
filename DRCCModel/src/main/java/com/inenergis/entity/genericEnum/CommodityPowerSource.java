package com.inenergis.entity.genericEnum;

public enum CommodityPowerSource {
    SOLAR("Solar"),
    WIND("Wind"),
    ON_SITE("On-Site"),
    TBD("TBD");

    private String name;

    CommodityPowerSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}