package com.inenergis.controller.program;

import com.inenergis.controller.model.RatePlanEligibility;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.exception.BusinessException;
import com.inenergis.service.RatePlanEligibilityService;
import com.inenergis.service.RatePlanService;
import com.inenergis.util.UIMessage;

import javax.faces.application.FacesMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public abstract class RateEnrollmentHandler {
    private BaseServiceAgreement serviceAgreement;

    public abstract RatePlanService getRatePlanService();
    public abstract BaseServiceAgreement getServiceAgreement();
    public abstract UIMessage getUiMessage();
    public abstract RatePlanEligibilityService getRatePlanEligibilityService();

    public List<RatePlanEligibility> generateRatePlans() {
        List<RatePlan> rates = getRatePlanService().getAll().stream().filter( ratePlan -> ratePlan.isActive() && ratePlan.getActiveProfile() != null && ratePlan.getActiveProfile().isActive()).collect(Collectors.toList());
        List<RatePlanEligibility> ratePlans = new ArrayList<>();
        for (RatePlan rate : rates) {
            ratePlans.add(createEligibility(rate));
        }
        return ratePlans;
    }

    public List<RatePlanEligibility> generateRatePlans( String Commodity) {
        System.out.println(Commodity);

        List<RatePlan> rates = getRatePlanService().getAll().stream().filter( ratePlan -> ratePlan.isActive() && ratePlan.getCommodityType().toString().equals(Commodity)  && ratePlan.getActiveProfile() != null && ratePlan.getActiveProfile().isActive()).collect(Collectors.toList());
        List<RatePlanEligibility> ratePlans = new ArrayList<>();
        for (RatePlan rate : rates) {
            ratePlans.add(createEligibility(rate));
        }
        return ratePlans;
    }


    private RatePlanEligibility createEligibility( RatePlan ratePlan) {
        RatePlanEligibility eligibility = new RatePlanEligibility();
        RatePlanEligibilityService.Eligibility eligibilityTest = getRatePlanEligibilityService().checkEligibility(getServiceAgreement(), ratePlan);
        eligibility.setRatePlan(ratePlan);
        eligibility.setUnenrollable(getRatePlanService().findActiveRatePlanEnrollmentInServiceAgreement(getServiceAgreement(), ratePlan) != null);
        eligibility.setEligible(eligibilityTest.isEligible());
        eligibility.setIneligibleFields(eligibilityTest.getIneligibleReasons());
        eligibility.setEnrollable(eligibility.isEligible() && !eligibility.isUnenrollable());
        return eligibility;
    }
}
