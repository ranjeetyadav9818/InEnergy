package com.inenergis.entity.genericEnum;


public enum ContractAddressType {

    PRIMARY("Primary"),
    BILLING_ACCOUNTS_PAYABLE("Billing & Accounts Payable"),
    ALTERNATE_LOCATION("Alternate location");

    String label;

    ContractAddressType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
