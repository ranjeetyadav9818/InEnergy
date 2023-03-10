package com.inenergis.entity.genericEnum;

public enum LinkageType {
    RATE_PLANS("Rate Plans"),
    RESOURCES("Resources"),
    SUBLAPS("Sublaps"),
    SUBSTATIONS("Substations"),
    FEEDERS("Feeders");

    private String name;

    LinkageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {return getDeclaringClass().getCanonicalName() + '.' + name();}

}