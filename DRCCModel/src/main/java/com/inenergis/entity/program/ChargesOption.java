package com.inenergis.entity.program;

public enum ChargesOption {
    MANDATORY("Mandatory"),
    VOLUNTARY("Voluntary"),
    OTHER_CLIENT_ATTRIBUTES("Other client attributes");

    private String label;

    ChargesOption(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}


