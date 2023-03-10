package com.inenergis.entity.genericEnum;

public enum Vendor {
    APX("APX"),
    GENESYS("Genesys"),
    GRIDIUM("Gridium"),
    INTERNAL("Internal"),
    NUANCE("Nuance"),
    VENDOR_X("Vendor X");

    private String name;

    Vendor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}