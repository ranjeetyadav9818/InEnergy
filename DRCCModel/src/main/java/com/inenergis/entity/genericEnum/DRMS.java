package com.inenergis.entity.genericEnum;

public enum DRMS {
    APX("APX"),
    DRMS_X("DRMS X"),
    ENERGY_IP("Energy IP"),
    INTERACT("InterAct"),
    SEELOAD("SEELoad");

    private String name;

    DRMS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}