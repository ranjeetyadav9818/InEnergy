package com.inenergis.rest.model.voltageIndicator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class VoltageIndicatorResponse {

    @JsonProperty("VoltageIndicatorResponse")
    private VoltageIndicatorSPListOutput voltageIndicatorResponse;

}
