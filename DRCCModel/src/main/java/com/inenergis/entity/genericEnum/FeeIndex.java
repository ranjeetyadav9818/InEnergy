package com.inenergis.entity.genericEnum;

public enum FeeIndex {
    INDUSTRY_INDEX_A("Industry Index A"),
    INDUSTRY_INDEX_B("Industry Index B"),
    INDUSTRY_INDEX_C("Industry Index C");

    private String name;

    FeeIndex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
