package com.inenergis.rest.model.registrationDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class RegistrationDetailsRequest {

    @JsonProperty("RegistrationDetailsRequest")
    private RegistrationDetailsInput registrationDetailsRequest;
}
