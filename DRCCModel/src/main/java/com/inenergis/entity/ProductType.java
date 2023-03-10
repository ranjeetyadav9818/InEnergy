package com.inenergis.entity;

public enum ProductType {
    PDR("PDR"),
    RDRR("RDRR");

    private String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}