package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticRegistration implements SearchMatch {

    public static final String ELASTIC_TYPE = "registration";

    private Long id;

    private String isoRegistrationId;

    @Override
    public String getId() {
        return Long.toString(id);
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }

    @Override
    public String getType() {
        return ELASTIC_TYPE;
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
