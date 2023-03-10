package com.inenergis.model;

import lombok.Data;

import java.beans.Transient;


@Data
public class ElasticEventNotification implements SearchMatch {

    public static final String ELASTIC_TYPE = "event_notification";

    private Long id;
    private String name;
    private String options;
    private String eventType;

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
        return "EVENT_NOTIFICATION";
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
