package com.inenergis.entity.genericEnum;

public enum PercentageHierarchyType {
    COUNTRY("Country"),
    STATE("State"),
    COUNTY("County"),
    CITY("City");

    private String name;

    PercentageHierarchyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}