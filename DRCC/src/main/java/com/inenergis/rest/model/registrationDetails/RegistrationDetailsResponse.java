package com.inenergis.rest.model.registrationDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RegistrationDetailsResponse {

    @JsonProperty("RegistrationDetailsResponse")
    private RegistrationDetailsOutput registrationDetailsResponse;

}
