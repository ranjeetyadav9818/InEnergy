package com.inenergis.entity.program;

public enum ChargesEquipmentType {
    MV90("MV-90"),
    SMART_METER("Smart meter");

    private String label;

    ChargesEquipmentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}


