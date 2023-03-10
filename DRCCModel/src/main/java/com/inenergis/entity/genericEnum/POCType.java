package com.inenergis.entity.genericEnum;


public enum POCType {

    PRIMARY("Primary"),
    SUPPORTING("Supporting");

    private String label;

    public String getLabel() {
        return label;
    }

    POCType(String label) {
        this.label = label;
    }
}
