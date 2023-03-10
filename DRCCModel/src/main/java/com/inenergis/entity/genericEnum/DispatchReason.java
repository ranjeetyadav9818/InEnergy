package com.inenergis.entity.genericEnum;

public enum DispatchReason {
    RETAIL_PROGRAM_TRIGGER("Retail Program Trigger"),
    RETAIL_TRANSMISSION_OPS("Retail Transmission Ops"),
    RETAIL_EMERGENCY("Retail Emergency"),
    RETAIL_OTHER("Retail Other"),
    MARKET_AWARD("Market Award"),
    NOTIFICATION_TEST("Notification Test"),
    MARKET_AWARD_TEST("Market Award Test"),
    RETAIL_TEST("Retail Test"),
    RETAIL_RE_TEST("Retail Re-test");

    private String name;

    DispatchReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}