package com.inenergis.model;

import lombok.Data;

import java.beans.Transient;

@Data
public class ElasticAggregator implements SearchMatch {

    public static final String ELASTIC_TYPE = "aggregator";

    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String primaryPOC;
    private String phone;

    @Override
    public String getId() {
        return Long.toString(id);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getType() {
        return "AGGREGATOR";
    }

    @Override
    @Transient
    public String getIcon() {
        return "settings_input_component";
    }

    @Override
    public String getPermission() {
        return null;
    }
}