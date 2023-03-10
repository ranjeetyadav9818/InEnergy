package com.inenergis.network.pgerestclient.model;

import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class ResourceRegistrationRequestModel extends RequestModel {

    private final static String DEFAULT_PRODUCT = "ENERGY";
    private final static String URL_TOKEN = "pge.api.resourceregistration.url";

    private String resourceId;
    private String registrationId;
    private String resourceProductType;
    private String registrationStatus;
    private LocalDateTime registrationEffectiveStartDate;
    private LocalDateTime registrationEffectiveEndDate;
    private LocalDateTime resourceStartDate;
    private LocalDateTime resourceEndDate;
    private String product;

    public ResourceRegistrationRequestModel(RegistrationSubmissionStatus registrationSubmissionStatus) throws IOException {
        this.urlToken = URL_TOKEN;


        resourceId = registrationSubmissionStatus.getIsoResource().getName();
        registrationId = registrationSubmissionStatus.getIsoRegistrationId();
        resourceProductType = registrationSubmissionStatus.getIsoResource().getType().name();
        registrationStatus = registrationSubmissionStatus.getRegistrationStatus().toString();
        registrationEffectiveStartDate = registrationSubmissionStatus.getActiveStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        registrationEffectiveEndDate = registrationSubmissionStatus.getActiveEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        resourceStartDate = registrationEffectiveStartDate;
        resourceEndDate = LocalDateTime.of(9999,12,31,23,59);
        product = DEFAULT_PRODUCT;
    }
}