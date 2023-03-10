package com.inenergis.entity.program;

public enum ChargesCategory {
    EARLY_TERMINATION("Early Termination"),
    ENERGY("Energy"),
    ENERGY_EFFICIENCY("Energy Efficiency"),
    DEMAND("Demand"),
    DISTRIBUTION("Distribution"),
    DELIVERY("Delivery"),
    GREEN_ENERGY("Green Energy"),
    HYDRO_GENERATION("Hydro Generation"),
    INFRASTRUCTURE("Infrastructure"),
    INSTALLATION("Installation"),
    METER("Meter"),
    PEAK_DAY("Peak Day"),
    PROGRAM("Program"),
    RECOVERY("Recovery"),
    REPROGRAMMING("Reprogramming"),
    TAX("Tax"),
    TIME_OF("Time of"),
    TRANSMISSION("Transmission"),
    SOLAR("Solar"),
    RENEWABLE_ENERGY("Renewable Energy");

    private String label;

    ChargesCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}


