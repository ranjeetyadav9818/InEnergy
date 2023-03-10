package com.inenergis.entity;

public enum DataMappingType {
    SUBLAP("SUBLAP"),
    LSE("LSE"),
    METER_TYPE("Meter Type");

    private String name;

    DataMappingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {return getDeclaringClass().getCanonicalName() + '.' + name();}
}