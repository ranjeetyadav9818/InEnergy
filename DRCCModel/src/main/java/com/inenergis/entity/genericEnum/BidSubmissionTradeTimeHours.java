package com.inenergis.entity.genericEnum;


public enum BidSubmissionTradeTimeHours {

    TRADE_DAY_MINUS_ONE("Trade Day - 1"),
    TRADE_DAY("Trade Day"),
    TRADE_HOUR("Trade Hour");

    private String name;

    BidSubmissionTradeTimeHours(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
