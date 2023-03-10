package com.inenergis.entity.program;

public enum FeeType {
    FLAT("Flat"),
    PERCENTAGE("Percentage"),
    PRIMARY("Primary"),
    SECONDARY("Secondary"),
    TERTIARY("Tertiary"),
    TRANSMISSION("Transmission"),
    CLIENT_SPECIFIC("Client specific");

    private String label;

    FeeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
