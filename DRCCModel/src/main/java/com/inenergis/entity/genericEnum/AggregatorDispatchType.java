package com.inenergis.entity.genericEnum;

public enum AggregatorDispatchType {
    FULL_AGGREGATOR_PORTFOLIO("Full Aggregator Portfolio"),
    PARTIAL_AGGREGATOR_PORTFOLIO("Partial Aggregator Portfolio");

    private String name;

    AggregatorDispatchType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}