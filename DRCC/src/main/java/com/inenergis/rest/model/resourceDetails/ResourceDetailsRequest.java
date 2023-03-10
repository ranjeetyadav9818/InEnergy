package com.inenergis.rest.model.resourceDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class ResourceDetailsRequest {

    @JsonProperty("ResourceDetailsRequest")
    private ResourceDetailsList resourceDetailsRequest;

}
