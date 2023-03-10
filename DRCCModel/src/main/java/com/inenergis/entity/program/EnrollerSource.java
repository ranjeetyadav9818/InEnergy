package com.inenergis.entity.program;


public enum EnrollerSource {
    AGGREGATOR("Aggregator"), DIRECT_ACCESS_TPA("Direct Access/TPA"), DIRECT_ENROLL("Direct Enroll") ;

    private String label;

    EnrollerSource(String label){
       this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
