package com.inenergis.controller.model;

public enum CycleSelectionTypeEnum {

    LAST_CYCLE("Last Cycle"),
    CHOOSE_PERIOD("Choose period");

    public String getName() {
        return name;
    }

    private String name;

    CycleSelectionTypeEnum(String name) {
        this.name = name;
    }
}