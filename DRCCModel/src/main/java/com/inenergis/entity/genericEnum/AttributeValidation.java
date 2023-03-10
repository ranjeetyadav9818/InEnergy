package com.inenergis.entity.genericEnum;

public enum AttributeValidation {

    DATE("Date"),
    NUMBER("Number"),
    TEXT("Text");

    private String label;

    AttributeValidation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
