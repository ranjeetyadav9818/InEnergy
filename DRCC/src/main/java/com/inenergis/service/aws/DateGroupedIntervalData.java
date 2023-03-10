package com.inenergis.service.aws;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DateGroupedIntervalData {

    private BigDecimal usageValue;
    private String units;
    private String month;
    private String day;
    private String servicePointId;

}
