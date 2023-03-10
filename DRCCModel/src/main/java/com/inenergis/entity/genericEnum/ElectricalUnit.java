package com.inenergis.entity.genericEnum;

public enum ElectricalUnit {
    KW("kW"),
    MW("MW"),
    THERMS("Therms");

    private String name;

    ElectricalUnit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}