package com.inenergis.rest.model.resourceDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class ResourceDetailsRegistrationId {

    private String pgeRegistrationId;
    private String caisoRegistrationId;
    private String status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private List<ResourceDetailsSAId> saIds;

}
