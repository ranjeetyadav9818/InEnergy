package com.inenergis.model;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ServiceAgreementEligibility implements Serializable {

    private AgreementPointMap agreementPointMap;
    private boolean eligibile;
    private String ineligibleReason;
    private List<ProgramServiceAgreementEnrollment> applications;
    private BigDecimal firmServiceLevel;
    private ProgramAggregator aggregator;
    private String thirdPartyName;
    private ProgramServiceAgreementEnrollment currentProgramEnrolled;
}