package com.inenergis.entity.program;


public enum EventOption {

    CONSECUTIVE_DAYS("Consecutive days"),
    FOUR_HOUR_EVENT("4 hour event");

    private String label;

    EventOption(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
