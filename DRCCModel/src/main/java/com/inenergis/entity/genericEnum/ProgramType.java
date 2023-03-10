package com.inenergis.entity.genericEnum;

public enum ProgramType {

    RATE("Rate"),
    DEMAND_RESPONSE("Demand Response"),
    DISTRIBUTED_ENERGY_RESOURCE("Distributed Energy Resource"),
    ENERGY_EFFICIENCY("Energy Efficiency");

    private String label;


    ProgramType(String label) {
        this.label = label;
    }

    public String getLabel() {

        return label;
    }
}
