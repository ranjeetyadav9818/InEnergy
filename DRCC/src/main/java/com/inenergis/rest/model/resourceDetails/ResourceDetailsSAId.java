package com.inenergis.rest.model.resourceDetails;

import lombok.Data;

import java.util.List;


@Data
public class ResourceDetailsSAId {

    private String saId;
    private String saUUID;
    private List<ResourceDetailsSPId> spIds;

}
