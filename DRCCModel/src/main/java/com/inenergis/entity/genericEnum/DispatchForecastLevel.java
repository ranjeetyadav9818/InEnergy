package com.inenergis.entity.genericEnum;

public enum DispatchForecastLevel {
    SUBLAP("Sublap"),
    SUBSTATION("Substation"),
    FEEDER("Feeder"),
    RESOURCE("Resource");

    private String name;

    public String getName() {
        return name;
    }

    DispatchForecastLevel(String name) {this.name = name;}

    public String getId() {return getDeclaringClass().getCanonicalName() + '.' + name();}

}
