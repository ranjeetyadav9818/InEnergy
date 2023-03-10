package com.inenergis.controller.program;

import com.inenergis.controller.model.RatePlanEligibility;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.exception.BusinessException;
import com.inenergis.service.RatePlanEligibilityService;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.ServiceAgreementService;

import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class RateEligibilityController extends RateEnrollmentHandler implements Serializable {

    @Inject
    ServiceAgreementService serviceAgreementService;
    @Inject
    RatePlanService ratePlanService;

    @Inject
    RatePlanEligibilityService ratePlanEligibilityService;

    @Inject
    UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(RateEligibilityController.class);

    private String saId;
    private BaseServiceAgreement serviceAgreement;
    private List<RatePlanEligibility> ratePlans = new ArrayList<>();
    private RatePlanEnrollment tempEnrollment;
    private boolean showModal = false;

    @PostConstruct
    public void init() {
    }

    public void search() {

        serviceAgreement = serviceAgreementService.getById(saId);
        if(serviceAgreement !=null){

            String commoditySA = serviceAgreement.getDecriminatorValue();


            if (serviceAgreement == null) {

                uiMessage.addMessage("No service agreement found with id {0}", FacesMessage.SEVERITY_ERROR, saId);

            } else {

                ratePlans = generateRatePlans(commoditySA);
            }
        }
        else{
            uiMessage.addMessage("Program Commodity does not match with the Commodity of Service Agreement id {0}", FacesMessage.SEVERITY_ERROR, saId);
        }

    }


    public void enroll(RatePlanEligibility eligibility) {

        ComodityType commodityPlan = eligibility.getRatePlan().getCommodityType();
        String stringComm = commodityPlan.toString();
        serviceAgreement = serviceAgreementService.getById(saId);
        String commoditySA = serviceAgreement.getDecriminatorValue();

        if(stringComm.equals(commoditySA)){

            List<RatePlanEnrollment> enrollments = null;
            try {
                enrollments = ratePlanService.enroll(eligibility.getRatePlan(), serviceAgreement);
                getUiMessage().addMessage("Customer with SA {0} enrolled to {1}", getServiceAgreement().getServiceAgreementId(), generateIds(enrollments));
                search();
            } catch (BusinessException e) {
                log.error(e.getMessage());
                uiMessage.addMessage(e.getBusinessMessage() , FacesMessage.SEVERITY_ERROR);
                getUiMessage().addMessage(e.getBusinessMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo().toArray());
            }

        }else{

            uiMessage.addMessage("Commodity of Service Agreement not matched with the Commodity of Rate Plan  ", FacesMessage.SEVERITY_ERROR, saId);
        }



    }

    public String generateIds(List<RatePlanEnrollment> enrollments) {
        StringBuilder sb = new StringBuilder();
        for (RatePlanEnrollment enrollment : enrollments) {
            sb = sb.append(enrollment.getRatePlan().getName()).append(" ");
        }
        return
                sb.toString();
    }

    private void unenrollOnePlan(RatePlanEnrollment enrollment) {
        try {
            ratePlanService.unenroll(enrollment);
            getUiMessage().addMessage("Customer with SA {0} unenrolled from {1}", getServiceAgreement().getServiceAgreementId(), enrollment.getRatePlan().getActiveProfile().buildRateCodeId());
            search();
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getBusinessMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo().toArray());
        }
    }


    public void unenroll(RatePlanEligibility eligibility) {

        RatePlanEnrollment enrollment = ratePlanService.findActiveRatePlanEnrollmentInServiceAgreement(getServiceAgreement(), eligibility.getRatePlan());
        if (CollectionUtils.isEmpty(enrollment.getRatePlan().getActiveProfile().getApplicableRatePlans())) {
            unenrollOnePlan(enrollment);
        } else {
            askHowManyUnenrolls();
            tempEnrollment = enrollment;
        }

    }


    public void unenrollAll() {
        try {
            List<RatePlanEnrollment> unenrollments = ratePlanService.unenrollAndRelated(tempEnrollment, serviceAgreement);
            getUiMessage().addMessage("Customer with SA {0} unenrolled from {1}", getServiceAgreement().getServiceAgreementId(), generateIds(unenrollments));
            tempEnrollment = null;
            showModal = false;
            search();
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getBusinessMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo().toArray());
        }
    }


    public void unenrollOne() {
        unenrollOnePlan(tempEnrollment);
        tempEnrollment = null;
        showModal = false;
    }

    public void cancelUnenrollment() {
        tempEnrollment = null;
        showModal = false;
    }

    public void clear() {
        saId = null;
        serviceAgreement = null;
    }

    public void askHowManyUnenrolls() {
        showModal = true;
    }

}