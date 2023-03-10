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
public class ElasticAsset implements SearchMatch {

    public static final String ELASTIC_TYPE = "asset";

    private Long id;

    private String name;

    private String description;

    private String make;

    private String supplierPartNumber;

    private String model;

    private String assetType;

    private List<String> catalogAttributes;

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
        return "asset";
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