package com.inenergis.entity.program;


public enum FeeFrequency {

    PER_DAY("Per day"),
    PER_MONTH("Per month"),
    ONE_TIME("One time");


    private String label;

    FeeFrequency(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
