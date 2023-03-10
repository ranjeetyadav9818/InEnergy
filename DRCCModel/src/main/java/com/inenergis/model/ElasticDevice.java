package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticDevice implements SearchMatch {

    public static final String ELASTIC_TYPE = "device";

    private Long id;

    private String name;

    private String description;

    private Long externalId;

    private String address1;

    private String address2;

    private String address3;

    private String city;

    private String postcode;

    private String deviceType;

    private List<String> deviceAttributes;

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
        return "device";
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
