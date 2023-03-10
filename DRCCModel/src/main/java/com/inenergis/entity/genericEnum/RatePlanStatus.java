package com.inenergis.entity.genericEnum;

public enum RatePlanStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    TERMINATED("Terminated");

    private String name;

    RatePlanStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}