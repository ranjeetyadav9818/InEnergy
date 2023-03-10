package com.inenergis.entity.genericEnum;

public enum RatePercentageAncillaryApplicability {
    ALL("All charges"),
    SELECTED("Selected charges"),
    UNSELECTED("Not selected charges");

    private String name;

    RatePercentageAncillaryApplicability(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}