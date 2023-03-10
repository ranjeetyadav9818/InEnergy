package com.inenergis.entity.genericEnum;

public enum FeeDetailUnit {

    KW("kW"),
    MW("MW"),
    VOLTS("Volts");

    private String label;

    public String getLabel() {
        return label;
    }


    FeeDetailUnit(String label) {

        this.label = label;
    }
}
