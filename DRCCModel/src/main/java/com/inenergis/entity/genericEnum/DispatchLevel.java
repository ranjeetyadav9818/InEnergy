package com.inenergis.entity.genericEnum;

public enum DispatchLevel {
    ENTIRE_PROGRAM("Entire Program"),
    CUSTOMERS("Customers"),
    SUBLAP("Sublap"),
    SUBSTATION("Substation"),
    CIRCUIT("Circuit");

    private String name;

    DispatchLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {return getDeclaringClass().getCanonicalName() + '.' + name();}

}