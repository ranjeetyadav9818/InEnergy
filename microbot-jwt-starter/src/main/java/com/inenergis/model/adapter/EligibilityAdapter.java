package com.inenergis.model.adapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inenergis.entity.PortalUser;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.model.PeakDemandResponse;
import com.inenergis.util.FslRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egamas on 13/10/2017.
 */
@Data
@Builder
@AllArgsConstructor
public class EligibilityAdapter {

    public EligibilityAdapter() {
        servicePointsEligibility = new ArrayList<>();
        messages = new ArrayList<>();
    }

    List<String> messages;
    List<ServiceAgreementEligibilityAdapter> servicePointsEligibility;

    @JsonProperty("hasError")
    public boolean hasError(){
        return CollectionUtils.isNotEmpty(messages);
    }

    @JsonProperty("eligible")
    public boolean isEligible(){
        return ! hasError() &&! servicePointsEligibility.stream().filter(e -> !e.isEligible()).findFirst().isPresent();
    }

    @JsonIgnore
    private Program program;
    @JsonIgnore
    private ProgramProfile activeProfile;
    @JsonIgnore
    private ServiceAgreement serviceAgreement;
    @JsonIgnore
    private PortalUser portalUser;
    @JsonIgnore
    private PeakDemandResponse peakDemandResponse;

}
