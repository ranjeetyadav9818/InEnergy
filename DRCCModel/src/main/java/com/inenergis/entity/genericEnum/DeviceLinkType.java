package com.inenergis.entity.genericEnum;

public enum DeviceLinkType {
    IN("Inbound"),
    OUT("Outbound"),
    BIDIRECTIONAL("Bidirectional");

    private String name;

    DeviceLinkType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}