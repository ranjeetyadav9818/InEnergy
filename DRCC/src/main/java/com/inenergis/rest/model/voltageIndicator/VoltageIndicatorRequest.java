package com.inenergis.rest.model.voltageIndicator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class VoltageIndicatorRequest {

    @JsonProperty("VoltageIndicatorRequest")
    private VoltageIndicatorSPListInput voltageIndicatorRequest;

}
