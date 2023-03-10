package com.inenergis.entity.genericEnum;


public enum CompanySector {

    RENEWABLE_ENERGY_PROVIDER( "Renewable Energy Provider") ,
    UTILITY_COMPANY( "Utility Company") ,
    AGGREGATOR( "Aggregator") ,
    ISO("Market") ;

    private String label;

    CompanySector(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
