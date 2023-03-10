package com.inenergis.entity.program;

public enum GasCapUnit {

    Therms("Therms"),
    Customer("Customer");

    private String name;

    GasCapUnit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}