package com.inenergis.rest.model.resourceDetails;

import lombok.Data;

import java.util.List;


@Data
public class ResourceDetailsOutput {

    private String resourceId;
    private List<ResourceDetailsRegistrationId> registrationIds;
}
