package com.inenergis.entity.genericEnum;

public enum FslRuleComparisonReference {
    AVERAGE_SUMMER_ON_PEAK("Average Summer on-peak"),
    MAX_SUMMER_ON_PEAK("Max Summer on-peak"),
    AVERAGE_WINTER_PARTIAL_PEAK("Average Winter partial-peak"),
    MAX_WINTER_PARTIAL_PEAK("Max Winter partial-peak");

    private String name;

    FslRuleComparisonReference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}