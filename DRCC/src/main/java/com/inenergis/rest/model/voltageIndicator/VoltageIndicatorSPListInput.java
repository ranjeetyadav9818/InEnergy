package com.inenergis.rest.model.voltageIndicator;

import lombok.Data;

import java.util.List;


@Data
public class VoltageIndicatorSPListInput {

    private List<VoltageIndicatorInput> spIds;

}
