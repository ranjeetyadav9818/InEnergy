package com.inenergis.entity.genericEnum;

public enum BidAction {
    BID("Bid"),
    NO_BID("No Bid"),
    CANCEL("Cancel");

    private String name;

    BidAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}