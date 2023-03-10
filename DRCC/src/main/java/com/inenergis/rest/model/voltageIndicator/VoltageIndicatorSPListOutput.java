package com.inenergis.rest.model.voltageIndicator;

import lombok.Data;

import java.util.List;


@Data
public class VoltageIndicatorSPListOutput {

    private List<VoltageIndicatorOutput> spIds;

}
