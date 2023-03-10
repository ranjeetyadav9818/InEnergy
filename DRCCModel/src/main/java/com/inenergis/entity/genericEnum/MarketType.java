package com.inenergis.entity.genericEnum;

public enum MarketType {
    REAL_TIME("Real Time"),
    DAY_AHEAD("Day Ahead");

    private String name;

    MarketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}