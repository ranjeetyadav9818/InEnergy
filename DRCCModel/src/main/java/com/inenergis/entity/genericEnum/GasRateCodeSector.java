package com.inenergis.entity.genericEnum;

public enum GasRateCodeSector {
    RESIDENTIAL("Residential"),
    SMALL_COMMERCIAL_21_299_KW("Small Commercial"),
    MEDIUM_COMMERCIAL_500_999_KW("Medium Commercial"),
    LARGE_COMMERCIAL_1000_PLUS_KW("Large Commercial"),
    AGRICULTURAL("Agricultural");

    private String name;

    GasRateCodeSector(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

}