package com.inenergis.model;

import lombok.Data;

import java.beans.Transient;

@Data
public class ElasticEvent implements SearchMatch {

    public static final String ELASTIC_TYPE = "event";


    private Long id;
    private String name;
    private String program;
    private String drmsId;
    private String dispatchLevel;
    private String dispatchReason;

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
        return "EVENT";
    }

    @Override
    @Transient
    public String getIcon() {
        return "";
    }

    @Override
    public String getPermission() {
        return null;
    }
}
