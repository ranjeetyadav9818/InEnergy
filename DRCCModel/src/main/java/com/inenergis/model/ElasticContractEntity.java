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
public class ElasticContractEntity implements SearchMatch {

    public static final String ELASTIC_TYPE = "contractEntity";

    Long id;
    String businessName;
    String dba;
    String sector;
    String taxId;
    List<String> addresses;
    List<String> cities;
    List<String> states;
    List<String> postCodes;
    List<String> noteTitles;
    List<String> noteDescriptions;
    List<String> pocNames;
    List<String> pocTitles;
    List<String> pocPhones;
    List<String> pocEmails;

    @Override
    public String getId() {
        return Long.toString(id);
    }

    @Override
    public String toString() {
        return businessName;
    }

    @Override
    public String getType() {
        return "contractEntity";
    }

    @Override
    @Transient
    public String getIcon() {
        return "perm_contact_calendar";
    }

    @Override
    public String getPermission() {
        return null;
    }
}