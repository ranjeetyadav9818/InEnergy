package com.inenergis.model;

import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;

public final class ElasticLocationConverter {

    private ElasticLocationConverter() {
    }

    public static ElasticLocation convert(LocationSubmissionStatus location) {
        return ElasticLocation.builder()
                .id(location.getId())
                .lse(location.getIsoLse())
                .sublap(location.getIsoSublap ())
                .isoResourceId(location.getIsoResourceId())
                .build();
    }
}