package com.inenergis.rest.model.registrationDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class RegistrationDetailsOutput {

    private String pgeRegistrationId;
    private String caisoRegistrationId;
    private String status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private List<RegistrationDetailsSAId> saIds;

}
