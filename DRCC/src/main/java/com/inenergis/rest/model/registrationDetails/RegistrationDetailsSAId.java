package com.inenergis.rest.model.registrationDetails;

import lombok.Data;

import java.util.List;


@Data
public class RegistrationDetailsSAId {

    private String saId;
    private List<RegistrationDetailsSPId> spIds;

}
