package com.inenergis.entity.program;


public enum FeeBaseline {


    TOTAL_USAGE("Total Usage"),
    OVER_100_OF_BASELINE("Over 100% of Baseline"),
    TBD_CLIENT_SPECIFIC("TBD Client specific"),
    PEAK_LOAD_CONTRIBUTION("Peak Load Contribution"),
    DEMAND("Demand"),
    PER_METER("Per Meter"),
    PER_PREMISE("Per Premise");

    
    private String label;

    FeeBaseline(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
