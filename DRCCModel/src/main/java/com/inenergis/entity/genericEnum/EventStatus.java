package com.inenergis.entity.genericEnum;

public enum EventStatus {
    SUBMITTED("Submitted"),
    ERROR("ERROR"),
    CANCELLED("Cancelled"),
    TERMINATED("Terminated"),
    PLANNED("Planned"),
    RESERVED("Reserved");

    private String name;

    EventStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}