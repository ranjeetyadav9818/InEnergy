package com.inenergis.entity.genericEnum;
  public enum IsoApplicableContractEnum {
    SERVICE_AGREEMENT("Service Agreement"),
    CONTRACT("Contract");

    private String name;

    IsoApplicableContractEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
