package com.inenergis.entity.genericEnum;


public enum BidSubmissionTradeTime {

    TRADE_DAY_MINUS_ONE("Trade Day - 1"),
    TRADE_DAY("Trade Day");

    public String name;

    BidSubmissionTradeTime(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

