package com.inenergis.entity.genericEnum;


public enum DiscountType {

    DISCOUNT("Discount"),
    CREDIT("Credit");

    private String name;

    DiscountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
