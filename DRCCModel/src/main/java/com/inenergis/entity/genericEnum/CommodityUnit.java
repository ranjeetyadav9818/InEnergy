package com.inenergis.entity.genericEnum;

public enum CommodityUnit {
    KW("kW"),
    MW("MW"),
    VOLTS("Volts"),
    THERMES("Thermes"),
    GALLONS("Gallons");

    private String name;

    CommodityUnit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}