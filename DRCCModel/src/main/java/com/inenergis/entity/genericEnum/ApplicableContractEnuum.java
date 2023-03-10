package com.inenergis.entity.genericEnum;

public enum ApplicableContractEnuum {
//    SERVICEAGREEMENT("Service Agreement"),
//    CONTRACT("contract");
SERVICE_AGREEMENT("Service Agreement"),
    CONTRACT("Contract");

    private String name;

    ApplicableContractEnuum(String name) {


        this.name = name;
    }



    public String getName() {
        return name;
    }
}
