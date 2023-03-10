package com.inenergis.entity.genericEnum;

public enum NotificationType {
    SMS("SMS"),
    EMAIL("Email"),
    PHONE("Phone"),
    FAX("Fax");

    private String name;

    NotificationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}