package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticISO implements MultiSearchMatch {

    public static final String ELASTIC_TYPE = "iso";

    private Long id;
    private String name;

    private String udcId;
    private String drpId;
    private String scId;

    private List<String> profileNames;
    private List<String> productNames;
    private List<String> productTypes;
    private List<String> marketTypes;

    private String action;

    @Override
    public String getId() {
        return Long.toString(id);
    }

    @Override
    public String toString() {
        return action;
    }

    @Override
    public String getType() {
        return "ISO";
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

    @Override
    @Transient
    public String[] getActions() {
        return new String[]{"Market profile", "Locations", "Resource Search", "Resource configuration", "Bidding", "Award"};
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public MultiSearchMatch clone(String action) {
        return ElasticISO.builder()
                .id(id)
                .name(name)
                .udcId(udcId)
                .drpId(drpId)
                .scId(scId)
                .profileNames(profileNames)
                .productNames(productNames)
                .productTypes(productTypes)
                .marketTypes(marketTypes)
                .action(action)
                .build();
    }

    @Transient
    public String getUrl() {
        switch (action) {
            case "Market profile":
                return "IsoProfiles.xhtml";
            case "Locations":
                return "Locations.xhtml";
            case "Resource Search":
                return "Resources.xhtml";
            case "Resource configuration":
                return "ResourceModificationEvaluation.xhtml";
            case "Bidding":
                return "Bid.xhtml";
            case "Award":
                return "AwardSummary.xhtml";
            default:
                return "error.xhtml";
        }
    }
}
