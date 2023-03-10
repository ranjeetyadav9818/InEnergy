package com.inenergis.entity.program;

public enum EnrollmentAttribute {
    SAID("SAID","SAID"),
    UUID("UUID","UUID"),
    CUSTOMER_NAME("Customer Name","CUSTOMER_NAME"),
    BUSINESS_NAME("DBA Name","BUSINESS_NAME"),
    SERVICE_ADDRESS_1("Service Address 1","SERVICE_ADDRESS_1"),
    SERVICE_ADDRESS_2("Service Address 2","SERVICE_ADDRESS_2"),
    CITY("City","CITY"),
    STATE("State","STATE"),
    ZIP("Zip","ZIP"),
    METER_BADGE_NUMBER("Meter Badge Number","METER_BADGE_NUMBER"),
    AGGREGATOR_NAME("Aggregator Name","AGGREGATOR_NAME"),
    FSL("FSL", "FSL"),
    THIRD_PARTY_NAME("Third Party Name","THIRD_PARTY_NAME");

    private String label;
    private String id;

    EnrollmentAttribute(String label, String id) {
        this.label = label;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public String getId() {
        return id;
    }
}