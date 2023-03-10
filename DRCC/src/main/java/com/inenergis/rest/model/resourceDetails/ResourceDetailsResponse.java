package com.inenergis.rest.model.resourceDetails;

import lombok.Data;

import java.util.List;


@Data
public class ResourceDetailsResponse {

    private List<ResourceDetailsOutput> resourceIds;

}
