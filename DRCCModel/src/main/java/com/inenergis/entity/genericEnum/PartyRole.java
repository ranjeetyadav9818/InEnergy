package com.inenergis.entity.genericEnum;


public enum PartyRole {

    BUYER("Buyer"),
    SELLER("Seller");

    private String name;

    PartyRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
