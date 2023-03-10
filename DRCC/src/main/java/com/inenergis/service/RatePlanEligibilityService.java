package com.inenergis.service;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.genericEnum.GeneralEligibilityAttributeType;
import com.inenergis.entity.program.GeneralAvailability;
import com.inenergis.entity.program.GeneralAvailabilityApplicableValue;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanDemand;
import com.inenergis.entity.program.RatePlanProfile;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class RatePlanEligibilityService {

    @Inject
    RatePlanService ratePlanService;

    private StringBuilder ineligibleReasons = new StringBuilder();

    @Getter
    @Setter
    public class Eligibility {
        private boolean isEligible;
        private String ineligibleReasons;
    }

    public Eligibility checkEligibility(BaseServiceAgreement serviceAgreement) {
        Eligibility eligibility = new Eligibility();
        eligibility.isEligible = isEligible(serviceAgreement, ratePlanService.getAll());
        eligibility.ineligibleReasons = ineligibleReasons.toString();

        return eligibility;
    }

    public Eligibility checkEligibility(BaseServiceAgreement serviceAgreement, RatePlan ratePlan) {
        Eligibility eligibility = new Eligibility();
        eligibility.isEligible = isEligible(serviceAgreement, Collections.singletonList(ratePlan));
        eligibility.ineligibleReasons = ineligibleReasons.toString();

        return eligibility;
    }

    private boolean isEligible(BaseServiceAgreement serviceAgreement, List<RatePlan> ratePlans) {
        Boolean isEligible = true;
        ineligibleReasons = new StringBuilder();

        for (RatePlan ratePlan : ratePlans) {
            RatePlanProfile ratePlanProfile = ratePlan.getActiveProfile();
            if (ratePlanProfile == null) {
                continue;
            }

            for (GeneralAvailability generalAvailability : ratePlanProfile.getGeneralAvailabilities()) {
                isEligible = isGeneralAvailabilityEligible(generalAvailability, serviceAgreement) && isEligible;
            }

            for (RatePlanDemand demand : ratePlanProfile.getRatePlanDemands()) {
                isEligible = isDemandEligible(demand) && isEligible;
            }
        }

        return isEligible;
    }

    private void addIneligibleReason(GeneralEligibilityAttributeType type, String value) {
        ineligibleReasons.append(type.getName())
                .append(" (").append(value).append(")").append(System.getProperty("line.separator"));
    }

    private boolean isGeneralAvailabilityEligible(GeneralAvailability generalAvailability, BaseServiceAgreement serviceAgreement) {
        Boolean isEligible = true;

        List<String> applicableValues = generalAvailability.getApplicableValues().stream()
                .map(GeneralAvailabilityApplicableValue::getValue)
                .collect(Collectors.toList());

        String value = "";
        switch (generalAvailability.getAttributeType()) {
            case PREMISE_TYPE:
                value = serviceAgreement.getAgreementPointMaps().get(0).getServicePoint().getPremise().getPremiseType();
                break;
            case MTR_CONFIG_TYPE:
                value = ((ServicePoint)serviceAgreement.getAgreementPointMaps().get(0).getServicePoint()).getMeter().getConfigType();
                break;
            case HAS_3RD_PARTY_DRP:
                value = serviceAgreement.getHas3rdPartyDrp();
                break;
            case CUST_CLASS_CD:
                value = serviceAgreement.getCustClassCd();
                break;
            case CUST_SIZE:
                value = serviceAgreement.getCust_size();
                break;
            default:
                isEligible = false;
        }

        if (!applicableValues.contains(value)) {
            isEligible = false;
            addIneligibleReason(generalAvailability.getAttributeType(), value);
        }

        return isEligible;
    }

    private boolean isDemandEligible(RatePlanDemand demand) {
        return true;
    }
}
