package com.inenergis.entity.program;

public enum FeeableUnit {
    KW("kW"),
    KWH("kWh"),
    MW("MW"),
    MWH("MWH"),
    VOLTS("Volts"),
    METER("Meter"),
    PREMISE("Premise"),
    PERCENTAGE_OF_USAGE("Percentage of usage"),
    PERCENTAGE_OF_BILL("Percentage of bill");

    private String label;

    FeeableUnit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}