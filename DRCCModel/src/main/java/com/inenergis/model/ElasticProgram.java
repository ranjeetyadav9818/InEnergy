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
public class ElasticProgram implements MultiSearchMatch {

    public static final String ELASTIC_TYPE = "program";

    private Long id;
    private String name;

    private List<String> profileNames;
    private List<String> optionNames;
    private String isoProductMapping;
    private List<String> drmsProgramIds;


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
        return "ENERGY_PROGRAM";
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
        return new String[]{"Energy Resource Profile", "Program Management", "Eligibility & Enrollment"};
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public MultiSearchMatch clone(String action) {
        return ElasticProgram.builder()
                .name(name)
                .id(id)
                .action(action)
                .profileNames(profileNames)
                .optionNames(optionNames)
                .isoProductMapping(isoProductMapping)
                .drmsProgramIds(drmsProgramIds).build();
    }

    public String getUrl() {
        switch (action) {
            case "Energy Resource Profile":
                return "ProgramProfile.xhtml";
            case "Program Management":
                return "ProgramManagement.xhtml";
            case "Eligibility & Enrollment":
                return "EligibilityEnrollment.xhtml";
            default:
                return "error.xhtml";
        }
    }
}
