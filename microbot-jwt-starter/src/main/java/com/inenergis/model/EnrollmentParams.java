package com.inenergis.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by egamas on 17/10/2017.
 */
@Data
@NoArgsConstructor
public class EnrollmentParams {
    private String programId;
    private String selectedAggregator;
    private String fsl;
    private String thirdPartyName;
    private String servicePointId;
}
