package com.inenergis.entity.genericEnum;

public enum DispatchCancelReason {
    DUPLICATE_DISPATCH("Duplicate batch"),
    ELIMINATED_RETAIL_TRIGGER("Eliminated retail trigger"),
    ELIMINATED_TESTING_NEED("Eliminated testing need"),
    Other("Other");

    private String name;


    DispatchCancelReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}