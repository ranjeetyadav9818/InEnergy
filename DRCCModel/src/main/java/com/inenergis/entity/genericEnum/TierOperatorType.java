package com.inenergis.entity.genericEnum;

public enum TierOperatorType {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private String name;

    TierOperatorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}