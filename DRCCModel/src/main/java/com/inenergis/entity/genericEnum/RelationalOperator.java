package com.inenergis.entity.genericEnum;

public enum RelationalOperator {
    EQ("="),
    NE("≠"),
    LT("<"),
    LE("≤"),
    GE("≥"),
    GT(">");

    private String name;

    RelationalOperator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}