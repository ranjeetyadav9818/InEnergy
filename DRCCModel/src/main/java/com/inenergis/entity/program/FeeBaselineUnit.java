package com.inenergis.entity.program;


public enum FeeBaselineUnit {

    KW("kW"),
    KWH("kWh"),
    MW("MW"),
    MWH("MWh"),
    VOLTS("Volts");

    private String label;

    FeeBaselineUnit(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
