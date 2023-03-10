package com.inenergis.model;

import com.inenergis.entity.locationRegistration.IsoResource;

public final class ElasticResourceConverter {

    private ElasticResourceConverter() {
    }

    public static ElasticResource convert(IsoResource resource) {
        return ElasticResource.builder()
                .id(resource.getId())
                .name(resource.getName())
                .lse(resource.getIsoLse())
                .sublap(resource.getIsoSublap ())
                .build();
    }
}