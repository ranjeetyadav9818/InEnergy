package com.inenergis.model;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;

public final class ElasticRegistrationConverter {

    private ElasticRegistrationConverter() {
    }

    public static ElasticRegistration convert(RegistrationSubmissionStatus registration) {
        return ElasticRegistration.builder()
                .id(registration.getId())
                .isoRegistrationId(registration.getIsoRegistrationId())
                .build();
    }
}