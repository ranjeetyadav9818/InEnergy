package com.inenergis.entity.program;


public enum PremiseType {

    AG("AG"),
    APT("APT"),
    COM_IND("COM_IND"),
    COMM_CI("COMM_CI"),
    COMM_RES("COMM_RES"),
    DEL_PREM("DEL_PREM"),
    DEMOLISH("DEMOLISH"),
    ELE_GATE("ELE_GATE"),
    LIVEWORK("LIVEWORK"),
    MAILING("MAILING"),
    MH_PARK("MH PARK"),
    MOBILE("MOBILE"),
    OTHERFIX("OTHERFIX"),
    PARK_LOT("PARK_LOT"),
    PARK("PARK"),
    PUMP_AG("PUMP_AG"),
    PUMP_COM("PUMP_COM"),
    PUMP_DOM("PUMP_DOM"),
    RES("RES"),
    RESMULTI("RESMULTI"),
    SECDWEL("SECDWEL"),
    SIGN("SIGN"),
    ST_LITE("ST LITE"),
    TELECOM("TELECOM"),
    TEMP_SVC("TEMP SVC"),
    TRAFFIC("TRAFFIC"),
    UNKNOWN("UNKNOWN");

    private String label;

    PremiseType(String label){
       this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
