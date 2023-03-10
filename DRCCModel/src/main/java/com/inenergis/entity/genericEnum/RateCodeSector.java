package com.inenergis.entity.genericEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum RateCodeSector {
    RESIDENTIAL("Residential"),
    SMALL_COMMERCIAL_0_20_KW("Small Commercial (0-20 kw)"),
    SMALL_COMMERCIAL_21_299_KW("Small Commercial (21-299 kw)"),
    MEDIUM_COMMERCIAL_300_499_KW("Medium Commercial (300-499 kw)"),
    MEDIUM_COMMERCIAL_500_999_KW("Medium Commercial (500-999 kw)"),
    LARGE_COMMERCIAL_1000_PLUS_KW("Large Commercial (1,000+ kw)"),
    AGRICULTURAL("Agricultural"),
    LIGHTING("Lighting");

    private String name;

    RateCodeSector(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

}