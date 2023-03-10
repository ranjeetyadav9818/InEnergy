package com.inenergis.entity.genericEnum;

public enum CommodityProductType {

    ENERGY("Energy"),
    DEMAND("Demand"),
    ANCILLARY("Ancillary"),
    TRANSPORT("Transport"),
    BEHIND_METER_SERVICE("Behind Meter Service"),
    OTHER_OPPORTUNITIES("Other Opportunities");

    private String name;

    CommodityProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
