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
public class ElasticContract implements SearchMatch {

    public static final String ELASTIC_TYPE = "contract";

    private Long id;
    private String name;
    private String typeName;
    private List<String> programCommodityNames;
    private List<String> subProgramCommodityNames;
    private List<String> commodityTypes;
    private List<String> partyNames;
    private List<String> productTypes;
    private List<String> feeTypes;
    private List<String> indexes;
    private List<String> feeCategories;
    private List<String> creditCategories;
    private List<String> resourceNames;
    private List<String> serviceAgreements;
    private List<String> powerSources;

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
        return ELASTIC_TYPE;
    }

    @Override
    @Transient
    public String getIcon() {
        return "spellcheck";
    }

    @Override
    public String getPermission() {
        return null;
    }
}