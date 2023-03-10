package com.inenergis.entity.genericEnum;

public enum  RatePlanServiceType {

    TBD_CLIENT_SPECIFIC("TBD Client specific"),
    POLYPHASE_SERVICE("Polyphase Service"),
    SINGLE_PHASE_SERVICE("Single Phase Service");

    public String getLabel() {
        return label;
    }

    RatePlanServiceType(String label) {

        this.label = label;
    }

    private String label;
}
