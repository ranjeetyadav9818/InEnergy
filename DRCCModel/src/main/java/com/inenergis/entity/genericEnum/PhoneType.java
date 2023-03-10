package com.inenergis.entity.genericEnum;


public enum PhoneType {
    WORK("Work"),
    CELL("Cell"),
    OTHER("Other");
    private String label;

    PhoneType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}