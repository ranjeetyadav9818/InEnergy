package com.inenergis.controller.program;

import com.inenergis.commonServices.AgreementPointMapServiceContract;
import com.inenergis.commonServices.DataMappingServiceContract;
import com.inenergis.commonServices.EligibilityEngine;
import com.inenergis.commonServices.EnrollmentEngine;
import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.Layer7PeakDemandHistoryServiceContract;
import com.inenergis.commonServices.PeakDemandServiceCommon;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.genericEnum.EnrollmentChannel;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.exception.BusinessException;
import com.inenergis.model.PeakDemandResponse;
import com.inenergis.model.ServiceAgreementEligibility;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.ProgramAggregatorService;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.util.FslRange;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class EligibilityEnrollment implements Serializable {

    @Inject
    AgreementPointMapServiceContract agreementPointMapService;

    @Inject
    ProgramServiceContract programService;
    @Inject
    ProgramAggregatorService programAggregatorService;
    @Inject
    DataMappingServiceContract dataMappingService;
    @Inject
    UIMessage uiMessage;
    @Inject
    Identity identity;
    @Inject
    PropertyAccessor propertyAccessor;
    @Inject
    ServiceAgreementService serviceAgreementService;
    @Inject
    Layer7PeakDemandHistoryServiceContract layer7PeakDemandHistoryService;
    @Inject
    PeakDemandServiceCommon peakDemandService;
    @Inject
    JMSUtilContract jmsUtil;

    EligibilityEngine eligibilityEngine;
    EnrollmentEngine enrollmentEngine;

    Logger log = LoggerFactory.getLogger(EligibilityEnrollment.class);

    private String filterSAIDs;
    private String unenrollmentReason;
    private Program program;
    private ProgramProfile currentProfile;
    private List<ServiceAgreementEligibility> potentialServiceAgreements;
    private ServiceAgreementEligibility saEligibilityToEnroll;
    private ServiceAgreementEligibility saEligibilityToUnenroll;
    private String overridingReason;

    private FslRange fslRange;
    private boolean showFslOutOfRangeMessage = false;

    private PeakDemandResponse peakDemandResponse;

    @PostConstruct
    public void init() {
        program = programService.getProgram(Long.valueOf(ParameterEncoderService.getDefaultDecodedParameterAsLong()));
        eligibilityEngine = new EligibilityEngine();
        enrollmentEngine = new EnrollmentEngine();
        for (ProgramProfile profile : program.getProfiles()) {
            Date now = new Date();
            if (now.after(profile.getEffectiveStartDate()) && (profile.getEffectiveEndDate() == null || now.before(profile.getEffectiveEndDate()))) {
                currentProfile = profile;
                return;
            }
        }

        throw new IllegalArgumentException("This program has no active profile");
    }

    public void verify() {

        BaseServiceAgreement serviceAgreement = serviceAgreementService.getById(filterSAIDs);
        if(serviceAgreement != null){
            String commoditySA = serviceAgreement.getDecriminatorValue();

            ComodityType programCommodity = program.getCommodity();

            if (commoditySA.equals(programCommodity.toString())){

                potentialServiceAgreements = new ArrayList<>();
                final ArrayList<String> messages = new ArrayList();
                try {

                    peakDemandResponse = eligibilityEngine.verify(filterSAIDs, agreementPointMapService, peakDemandService, dataMappingService, potentialServiceAgreements, program.getActiveProfile(), program, messages);

                } catch (BusinessException e) {
                    log.error(e.getBusinessMessage());
                    uiMessage.addMessage(e.getBusinessMessage(), FacesMessage.SEVERITY_ERROR);
                }
                if (CollectionUtils.isNotEmpty(messages)) {
                    messages.stream().forEach(m -> {
                        uiMessage.addMessage(m, FacesMessage.SEVERITY_ERROR);
                    });
                }

            }else{

                uiMessage.addMessage("Program Commodity does not match with the Commodity of Service Agreement id {0}", FacesMessage.SEVERITY_ERROR, filterSAIDs);
            }
        }
        else{
            uiMessage.addMessage("No service agreement found with id {0}" , FacesMessage.SEVERITY_ERROR, filterSAIDs);
        }
//        String commoditySA = serviceAgreement.getDecriminatorValue();
//
//        ComodityType programCommodity = program.getCommodity();
//
//        if (commoditySA.equals(programCommodity.toString())){
//
//            potentialServiceAgreements = new ArrayList<>();
//            final ArrayList<String> messages = new ArrayList();
//            try {
//
//                peakDemandResponse = eligibilityEngine.verify(filterSAIDs, agreementPointMapService, peakDemandService, dataMappingService, potentialServiceAgreements, program.getActiveProfile(), program, messages);
//
//            } catch (BusinessException e) {
//                log.error(e.getBusinessMessage());
//                uiMessage.addMessage(e.getBusinessMessage(), FacesMessage.SEVERITY_ERROR);
//            }
//            if (CollectionUtils.isNotEmpty(messages)) {
//                messages.stream().forEach(m -> {
//                    uiMessage.addMessage(m, FacesMessage.SEVERITY_ERROR);
//                });
//            }
//
//        }else{
//
//            uiMessage.addMessage("Program Commodity does not match with the Commodity of Service Agreement id {0}", FacesMessage.SEVERITY_ERROR, filterSAIDs);
//        }


    }


    public void viewEnroll(ServiceAgreementEligibility serviceAgreementEligibility) {

        saEligibilityToEnroll = serviceAgreementEligibility;

        fslRange = enrollmentEngine.viewEnroll(serviceAgreementEligibility, currentProfile, peakDemandResponse);
    }

    public void viewUnenroll(ServiceAgreementEligibility serviceAgreementEligibility) {
        saEligibilityToUnenroll = serviceAgreementEligibility;
    }

    public void cancel() {
        saEligibilityToEnroll = null;
        saEligibilityToUnenroll = null;
        showFslOutOfRangeMessage = false;
        if (filterSAIDs != null) {
            verify();
        }
    }

    public List<ProgramAggregator> getActiveAggregators() {
        return programAggregatorService.getAllActive();
    }

    public void enroll() throws ParseException {
        final EnrollmentEngine.EnrollResult enroll = enrollmentEngine.enroll(
                saEligibilityToEnroll, program, peakDemandResponse,
                fslRange, overridingReason, eligibilityEngine, programService,
                peakDemandService, currentProfile, layer7PeakDemandHistoryService,
                ((User) identity.getAccount()).getEmail(), dataMappingService, EnrollmentChannel.DRCC.getLabel(), "Web form", jmsUtil);
        showFslOutOfRangeMessage = enroll.isShowFslOutOfRangeMessage();

        if (CollectionUtils.isNotEmpty(enroll.getMessages())) {
            enroll.getMessages().forEach(m -> uiMessage.addMessage(m, FacesMessage.SEVERITY_ERROR));
        } else {
            uiMessage.addMessage("Customer {0} enrolled to {1}", saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement().getAccount().getPerson().getCustomerName(), program.getName());
            cancel();
        }
    }

    public void unenroll() {
        try {
            saEligibilityToUnenroll.getCurrentProgramEnrolled().setEnrollmentStatus(EnrolmentStatus.UNENROLLED.getName());
            saEligibilityToUnenroll.getCurrentProgramEnrolled().setUnenrollReason(unenrollmentReason);
            saEligibilityToUnenroll.getCurrentProgramEnrolled().setEffectiveEndDate(new Date());
            programService.updateApplicationEnrollment(saEligibilityToUnenroll.getCurrentProgramEnrolled(), ((User) identity.getAccount()).getEmail());
            uiMessage.addMessage("Customer {0} unenrolled from {1}", saEligibilityToUnenroll.getAgreementPointMap().getServiceAgreement().getAccount().getPerson().getCustomerName(), program.getName());
            cancel();
        } catch (JMSException e) {
            log.error("Exception connecting to JMS server", e);
            uiMessage.addMessage("Customer {0} was not unenrolled due to an error on the server, please try again later or contact your administrator",
                    FacesMessage.SEVERITY_ERROR, saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement().getAccount().getPerson().getBusinessName());
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo());
        }
    }

    public boolean isAttributeRendered(String attribute) {
        return currentProfile.isAttributeInTheProgramEnrollmentAttributeList(attribute);
    }

    public void hideFslOutOfRangeMessage() {
        showFslOutOfRangeMessage = false;
    }
}