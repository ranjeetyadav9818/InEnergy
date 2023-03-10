package com.inenergis.entity.genericEnum;

public enum Season {
    WINTER("Winter"),
    SUMMER("Summer");

    private String name;

    Season(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
