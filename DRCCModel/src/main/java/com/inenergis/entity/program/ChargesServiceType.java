package com.inenergis.entity.program;

public enum ChargesServiceType {
    SINGLE_PHASE("Single phase service"),
    POLYPHASE("Polyphase service");

    private String label;

    ChargesServiceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}


