package com.inenergis.commonServices;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.genericEnum.EnrollmentChannel;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.EnrollmentAttribute;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramEligibilitySnapshot;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import com.inenergis.exception.BusinessException;
import com.inenergis.model.EligibilityResult;
import com.inenergis.model.PeakDemand;
import com.inenergis.model.PeakDemandResponse;
import com.inenergis.model.ServiceAgreementEligibility;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.FslRange;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by egamas on 14/10/2017.
 */
public class EnrollmentEngine {

    Logger log = LoggerFactory.getLogger(EnrollmentEngine.class);

    public FslRange viewEnroll(ServiceAgreementEligibility serviceAgreementEligibility,
                               ProgramProfile currentProfile, PeakDemandResponse peakDemandResponse) {
        BaseServiceAgreement serviceAgreement = serviceAgreementEligibility.getAgreementPointMap().getServiceAgreement();
        if(serviceAgreement.getDecriminatorValue().equals(ComodityType.Electricity)){
            return new FslRange(currentProfile.getFslRules(),
                    peakDemandResponse.getFslAverageSummerOnPeakWattList(serviceAgreement, currentProfile.getFlsTimeHorizon()),
                    peakDemandResponse.getFslAverageWinterPartialPeakWattList(serviceAgreement, currentProfile.getFlsTimeHorizon()));
        }
        else{
            return new FslRange(currentProfile.getFslRules(),
                    peakDemandResponse.getGasFslAverageSummerOnPeakWattList(serviceAgreement, currentProfile.getFlsTimeHorizon()),
                    peakDemandResponse.getGasFslAverageWinterPartialPeakWattList(serviceAgreement, currentProfile.getFlsTimeHorizon()));
        }
    }

    public EnrollResult enroll(ServiceAgreementEligibility saEligibilityToEnroll, Program program, PeakDemandResponse peakDemandResponse,
                               FslRange fslRange, String overridingReason, EligibilityEngine eligibilityEngine, ProgramServiceContract programService,
                               PeakDemandServiceCommon peakDemandService, ProgramProfile currentProfile,
                               Layer7PeakDemandHistoryServiceContract layer7PeakDemandHistoryService, String email,
                               DataMappingServiceContract dataMappingService, String enrollmentChannel, String enrollmentSource, JMSUtilContract jmsUtil) throws ParseException {
        EnrollResult result = new EnrollResult();
        ProgramServiceAgreementEnrollment saEnrollment = new ProgramServiceAgreementEnrollment();
        saEnrollment.setAggregator(saEligibilityToEnroll.getAggregator());
        saEnrollment.setEnrollmentChannel(enrollmentChannel);
        saEnrollment.setEnrollmentSource(enrollmentSource);
        saEnrollment.setProgram(program);
        saEnrollment.setServiceAgreement(saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement());
        saEnrollment.setEnrollmentStatus(EnrolmentStatus.IN_PROGRESS.getName());
        saEnrollment.setThirdPartyName(saEligibilityToEnroll.getThirdPartyName());
        saEnrollment.setAverageSummerOnPeakWatt(peakDemandResponse.getAverageSummerOnPeakWatt( saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement()));
        ProgramFirmServiceLevel fsl = getFsl(saEnrollment, saEligibilityToEnroll, email);
        if (fsl != null) {
            if (!fslRange.isInRange(fsl) && StringUtils.isEmpty(overridingReason)) {
                result.showFslOutOfRangeMessage = true;
                if (EnrollmentChannel.PORTAL.getLabel().equals(enrollmentChannel)){
                    result.messages.add(MessageFormat.format("Customer {0} was not enrolled. Firm Service Level out of range",
                            saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement().getAccount().getPerson().getCustomerName()));
                    return result;
                }
            }
            saEnrollment.setFsls(Collections.singletonList(fsl));
        }
        boolean capEligible = eligibilityEngine.isCapEligible(programService, program.getActiveProfile(), saEnrollment);
        if (!capEligible) {
            result.messages.add(MessageFormat.format("Customer {0} was not enrolled because the program CAP would be violated",
                    saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement().getAccount().getPerson().getCustomerName()));
            return result;
        }
        assignEligibilitySnapshots(saEnrollment, saEligibilityToEnroll.getAgreementPointMap(),
                peakDemandResponse, eligibilityEngine,
                dataMappingService, currentProfile, overridingReason, email);

        if (fsl != null) {
            saEnrollment.getSnapshots().add(fsl.generateSnapshot(fslRange.isInRange(fsl), overridingReason));
        }
        try {
            peakDemandService.savePeakDemandResponses(peakDemandResponse, layer7PeakDemandHistoryService);
            programService.saveProgramEnrollment(saEnrollment, saEligibilityToEnroll.getAgreementPointMap(), currentProfile, jmsUtil);
        } catch (JMSException e) {
            log.error("Exception connecting to JMS server", e);
            result.messages.add(MessageFormat.format("Customer {0} was not enrolled due to an error on the server, please try again later or contact your administrator",
                    saEligibilityToEnroll.getAgreementPointMap().getServiceAgreement().getAccount().getPerson().getCustomerName()));
        } catch (BusinessException e) {
            log.error(e.getMessage());
            result.messages.add(MessageFormat.format(e.getMessage(), e.getBusinessInfo()));
        }
        return result;
    }

    private void assignEligibilitySnapshots(ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap agreementPointMap,
                                            PeakDemandResponse peakDemandResponse, EligibilityEngine eligibilityEngine,
                                            DataMappingServiceContract dataMappingService,
                                            ProgramProfile currentProfile, String overridingReason, String email
    ) {
        List<PeakDemand> peakDemandsGenericList = peakDemandResponse.getPeakDemandGenericList(agreementPointMap.getServiceAgreement());

        final EligibilityResult eligible = eligibilityEngine.isEligible(dataMappingService, agreementPointMap, currentProfile, peakDemandsGenericList);
        saEnrollment.setSnapshots(new ArrayList<>());
        saEnrollment.getSnapshots().addAll(ProgramEligibilitySnapshot.createSnapshots(eligible, saEnrollment, overridingReason, email));
    }

    private ProgramFirmServiceLevel getFsl(ProgramServiceAgreementEnrollment saEnrollment, ServiceAgreementEligibility saEligibilityToEnroll
            , String email
    ) throws ParseException {
        ProgramFirmServiceLevel fsl = null;
        if (saEligibilityToEnroll.getFirmServiceLevel() != null) {
            fsl = new ProgramFirmServiceLevel();
            fsl.setApplication(saEnrollment);
            fsl.setEffectiveStartDate(new Date());
            fsl.setLastUpdated(new Date());
            fsl.setLastUpdatedBy(email);
            fsl.setValue(saEligibilityToEnroll.getFirmServiceLevel());
        }

        return fsl;
    }

    @Getter
    public class EnrollResult {
        private boolean showFslOutOfRangeMessage = false;
        private List<String> messages;

        public EnrollResult() {
            messages = new ArrayList<>();
        }
    }

    public void saveProgramEnrollment(ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap agreementPointMap,
                                      ProgramProfile currentProfile, EnrollmentServiceContract enrollmentService,
                                      WorkPlanServiceContract workflowService, JMSUtilContract jmsUtil) throws BusinessException, JMSException {
        final List<ProgramServiceAgreementEnrollment> saAlreadyEnrolled = enrollmentService.getBySaIdStatusesAndProgram(saEnrollment.getServiceAgreement().getServiceAgreementId(), Arrays.asList(EnrolmentStatus.IN_PROGRESS, EnrolmentStatus.IN_PROGRESS), currentProfile.getProgram());
        if (saAlreadyEnrolled != null && !saAlreadyEnrolled.isEmpty()) {
            throw new BusinessException(BusinessException.ExceptionCode.ALREADY_ENROLLED, "SA id {0} already enrolled ", Arrays.asList(saEnrollment.getServiceAgreement().getServiceAgreementId()));
        }
        enrollmentService.saveOrUpdate(saEnrollment);
        List<ProgramServiceAgreementEnrollmentAttribute> attributes = generateInitialAttributes(saEnrollment, agreementPointMap, currentProfile);
        for (ProgramServiceAgreementEnrollmentAttribute attribute : attributes) {
            enrollmentService.saveOrUpdateAttribute(attribute);
        }
        workflowService.createProgramEnrollmentPlan(saEnrollment, jmsUtil);
    }

    private List<ProgramServiceAgreementEnrollmentAttribute> generateInitialAttributes(ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap apm, ProgramProfile currentProfile) {
        List<ProgramServiceAgreementEnrollmentAttribute> result = new ArrayList<>();
        for (EnrollmentAttribute attribute : EnrollmentAttribute.values()) {
            if (currentProfile.isAttributeInTheProgramEnrollmentAttributeList(attribute.name())) {
                result.add(ProgramServiceAgreementEnrollmentAttribute.builder().enrollment(saEnrollment).key(attribute.getLabel()).value(getAttributeValue(attribute, saEnrollment, apm)).build());
            }
        }
        return result;
    }

    private String getAttributeValue(EnrollmentAttribute attribute, ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap apm) {
        switch (attribute) {
            case SAID:
                return apm.getServiceAgreement().getServiceAgreementId();
            case UUID:
                return apm.getServiceAgreement().getSaUuid();
            case BUSINESS_NAME:
                return apm.getServiceAgreement().getAccount().getPerson().getBusinessName();
            case CUSTOMER_NAME:
                return apm.getServiceAgreement().getAccount().getPerson().getCustomerName();
            case SERVICE_ADDRESS_1:
                return apm.getServicePoint().getPremise().getServiceAddress1();
            case SERVICE_ADDRESS_2:
                return apm.getServicePoint().getPremise().getServiceAddress2();
            case STATE:
                return apm.getServicePoint().getPremise().getServiceState();
            case CITY:
                return apm.getServicePoint().getPremise().getServiceCityUpr();
            case ZIP:
                return apm.getServicePoint().getPremise().getServicePostal();
            case AGGREGATOR_NAME:
                return saEnrollment.getAggregator() != null ? saEnrollment.getAggregator().getName() : null;
            case FSL:
                return saEnrollment.getLastFSL() != null ? saEnrollment.getLastFSL().getFormattedValue(ConstantsProviderModel.LOCALE) : null;
            case THIRD_PARTY_NAME:
                return saEnrollment.getThirdPartyName();
            case METER_BADGE_NUMBER:
                return (apm.getServicePoint()).getMeter().getBadgeNumber();
        }
        return "invalid value";
    }
}
