package com.inenergis.entity.genericEnum;

public enum PaymentType {
    BANK_TRANSFER("Bank Transfer"),
    CHEQUE("Cheque"),
    DIRECT_DEBIT("Direct Debit"),
    DEBIT_CARD("Debit Card");

    private String name;

    PaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}