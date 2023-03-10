package com.inenergis.entity.genericEnum;

public enum TaskType {
    USER("User"),
    SYS("System");

    private String name;

    TaskType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}