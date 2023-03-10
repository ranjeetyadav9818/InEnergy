package com.inenergis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inenergis.commonServices.AgreementPointMapServiceContract;
import com.inenergis.commonServices.DataMappingServiceContract;
import com.inenergis.commonServices.EligibilityEngine;
import com.inenergis.commonServices.EnrollmentEngine;
import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.Layer7PeakDemandHistoryServiceContract;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.dao.ProgramDao;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.entity.AgreementPointMapPK;
import com.inenergis.entity.PortalUser;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.EnrollmentChannel;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.exception.BusinessException;
import com.inenergis.model.EnrollmentParams;
import com.inenergis.model.ServiceAgreementEligibility;
import com.inenergis.model.adapter.AggregatorAdapter;
import com.inenergis.model.adapter.EligibilityAdapter;
import com.inenergis.model.adapter.EnrollmentAttributeAdapter;
import com.inenergis.model.adapter.ProgramAdapter;
import com.inenergis.model.adapter.ServiceAgreementEligibilityAdapter;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 09/10/2017.
 */
@Getter
@Setter
@Component
public class EligibilityEnrollmentService {

    @Autowired
    ProgramDao programDao;

    @Autowired
    ProgramServiceAgreementEnrollmentDao enrollmentDao;

    @Autowired
    Layer7PeakDemandHistoryServiceContract layer7PeakDemandHistoryService;

    @Autowired
    PeakDemandService peakDemandService;

    @Autowired
    PortalUserService userService;

    @Autowired
    AgreementPointMapServiceContract agreementPointMapService;

    @Autowired
    DataMappingServiceContract dataMappingService;

    @Autowired
    AggregatorService aggregatorService;

    @Autowired
    ProgramServiceContract programService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JMSUtilContract jmsUtil;

    private EnrollmentEngine enrollmentEngine = new EnrollmentEngine();
    private EligibilityEngine eligibilityEngine = new EligibilityEngine();

//    private PeakDemandResponse peakDemandResponse;

    Logger log = LoggerFactory.getLogger(EligibilityEnrollmentService.class);


    @Transactional("mysqlTransactionManager")
    public String getAvailableProgramsToEnroll(String email) throws JsonProcessingException {
        List<Program> activePrograms = programDao.getProgramsByActiveEquals(true);
        if (CollectionUtils.isNotEmpty(activePrograms)) {
            activePrograms= activePrograms.stream().filter(p -> p.getActiveProfile() != null).collect(Collectors.toList());
        }
        final PortalUser user = userService.getByEmail(email);
        if (CollectionUtils.isNotEmpty(activePrograms)) {
            final List<ProgramAdapter> adapteePrograms = activePrograms.stream().map(ProgramAdapter::build).collect(Collectors.toList());
            adapteePrograms.removeAll(getCurrentWithEnrollment(user.getEmail()));
            return objectMapper.writeValueAsString(adapteePrograms);
        }
        return StringUtils.EMPTY;
    }

    @Transactional("mysqlTransactionManager")
    public String getCurrentEnrolled(String email) throws JsonProcessingException {
        final PortalUser user = userService.getByEmail(email);
        return objectMapper.writeValueAsString(getCurrentEnrolledByStatus(user,EnrolmentStatus.ENROLLED));
    }

    @Transactional("mysqlTransactionManager")
    public List<ProgramAdapter> getCurrentWithEnrollment(String email) throws JsonProcessingException {
        final PortalUser user = userService.getByEmail(email);
        return getCurrentEnrolled(user);
    }

    @Transactional("mysqlTransactionManager")
    public String getCurrentPending(String email) throws JsonProcessingException {
        final PortalUser user = userService.getByEmail(email);
        return objectMapper.writeValueAsString(getCurrentEnrolledByStatus(user,EnrolmentStatus.IN_PROGRESS));
    }

    private List<ProgramAdapter> getCurrentEnrolledByStatus(PortalUser user, EnrolmentStatus status) {
        final List<ProgramServiceAgreementEnrollment> enrollments = user.getServiceAgreement().getEnrollments();
        if (CollectionUtils.isNotEmpty(enrollments)) {
            return enrollments.stream().filter(e -> status.getName().equals(e.getEnrollmentStatus())).map(e -> e.getProgram()).map(ProgramAdapter::build).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<ProgramAdapter> getCurrentEnrolled(PortalUser user) {
        final List<ProgramServiceAgreementEnrollment> enrollments = user.getServiceAgreement().getEnrollments();
        if (CollectionUtils.isNotEmpty(enrollments)) {
            return enrollments.stream().map(e -> e.getProgram()).map(ProgramAdapter::build).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Transactional("mysqlTransactionManager")
    public String checkEligibility(String user, String programId) throws JsonProcessingException {
        final EligibilityAdapter result = new EligibilityAdapter();
        validateUser(user, result);
        validateProgram(programId, result);
        if (!result.hasError()) {
            EligibilityEngine eligibilityEngine = new EligibilityEngine();
            List<ServiceAgreementEligibility> potentialServiceAgreements = new ArrayList<>();
            final ArrayList<String> messages = new ArrayList();
            try {
                result.setPeakDemandResponse(eligibilityEngine.verify(result.getServiceAgreement().getServiceAgreementId(), agreementPointMapService, peakDemandService, dataMappingService, potentialServiceAgreements, result.getActiveProfile(), result.getProgram(), messages));
                result.setServicePointsEligibility(potentialServiceAgreements.stream().map(eligibility -> ServiceAgreementEligibilityAdapter.build(eligibility)).collect(Collectors.toList()));
            } catch (BusinessException e) {
                log.error(e.getBusinessMessage());
                result.getMessages().add(e.getBusinessMessage());
            }
            if (CollectionUtils.isNotEmpty(messages)) {
                result.getMessages().addAll(messages);
            }
        }
        return objectMapper.writeValueAsString(result);
    }

    private EligibilityAdapter validateProgram(String programId, EligibilityAdapter result) {
        if (!NumberUtils.isNumber(programId)) {
            result.getMessages().add(("programId is not a number"));
            return result;
        }
        Long id = Long.valueOf(programId);
        Program program = programDao.getById(id);
        if (program == null) {
            result.getMessages().add(("program not found"));
            return result;
        }
        result.setProgram(program);
        final ProgramProfile activeProfile = program.getActiveProfile();
        if (activeProfile == null) {
            result.getMessages().add(("The program has no active profile"));
            return result;
        }
        result.setActiveProfile(activeProfile);
        return result;
    }

    private EligibilityAdapter validateUser(String user, EligibilityAdapter result) {
        final PortalUser portalUser = userService.getByEmail(user);
        if (portalUser == null) {
            result.getMessages().add(("User does not exist"));
            return result;
        }
        result.setPortalUser(portalUser);
        final ServiceAgreement serviceAgreement = portalUser.getServiceAgreement();
        if (serviceAgreement == null) {
            result.getMessages().add(("User do not have service agreement"));
            return result;
        }
        result.setServiceAgreement(serviceAgreement);
        return result;
    }

    @Transactional("mysqlTransactionManager")
    public String enroll(String email, EnrollmentParams attributes) throws JsonProcessingException, ParseException {
        final EligibilityAdapter eligibilityValidations = new EligibilityAdapter();
        validateUser(email, eligibilityValidations);
        validateProgram(attributes.getProgramId(), eligibilityValidations);
        final ProgramAdapter result = ProgramAdapter.build(eligibilityValidations.getProgram());
        if (eligibilityValidations.hasError()) {
            result.getMessages().addAll(eligibilityValidations.getMessages());
            return objectMapper.writeValueAsString(result);
        }
        validateFsl(attributes.getFsl(), result);
        validateAggregator(attributes.getSelectedAggregator(), result);
        validateServicePoint(attributes.getServicePointId(), eligibilityValidations.getServiceAgreement(), result);
        if(result.hasError()){
            return objectMapper.writeValueAsString(result);
        }

        final EligibilityAdapter preEnrollResult = preEnroll(email, attributes.getProgramId(), attributes.getServicePointId());
        if (CollectionUtils.isNotEmpty(preEnrollResult.getServicePointsEligibility())) {
            final ServiceAgreementEligibilityAdapter servicePointToEnroll = preEnrollResult.getServicePointsEligibility().get(0); //Taking the first one

            final ServiceAgreementEligibility eligibility = servicePointToEnroll.getServiceAgreementEligibility();
            if (StringUtils.isNotEmpty(attributes.getSelectedAggregator())) {
                eligibility.setAggregator(aggregatorService.getById(Long.parseLong(attributes.getSelectedAggregator())));
            }
            if (StringUtils.isNotEmpty(attributes.getThirdPartyName())) {
                eligibility.setThirdPartyName(attributes.getThirdPartyName() == null ? StringUtils.EMPTY : attributes.getThirdPartyName());
            }
            if (StringUtils.isNotEmpty(attributes.getFsl())) {
                eligibility.setFirmServiceLevel(BigDecimal.valueOf(Integer.parseInt(attributes.getFsl())));
            }
            final EnrollmentEngine.EnrollResult enroll = enrollmentEngine.enroll(
                    eligibility, preEnrollResult.getProgram(), preEnrollResult.getPeakDemandResponse(),
                    servicePointToEnroll.getFslRange(), StringUtils.EMPTY, eligibilityEngine, programService,
                    peakDemandService, preEnrollResult.getActiveProfile(), layer7PeakDemandHistoryService,
                    email, dataMappingService, EnrollmentChannel.PORTAL.getLabel(), "Web form",jmsUtil);
            result.getMessages().addAll(enroll.getMessages());
            if (enroll.isShowFslOutOfRangeMessage()) {
                result.getMessages().add("FSL is out of range");
            } else {
                result.setEnrollmentStatus(EnrolmentStatus.IN_PROGRESS.getName());
            }
        }
        return objectMapper.writeValueAsString(result);
    }

    private void validateServicePoint(String servicePointId, ServiceAgreement serviceAgreement, ProgramAdapter result) {
        if (!NumberUtils.isNumber(servicePointId)) {
            result.getMessages().add("servicePointId must be a number");
            return;
        }
        if (serviceAgreement != null && CollectionUtils.isNotEmpty(serviceAgreement.getAgreementPointMaps())) {
            final boolean spBelongsToSa = serviceAgreement.getAgreementPointMaps().stream().map(apm -> apm.getServicePoint().getServicePointId()).filter(spId -> spId.equals(servicePointId)).findFirst().isPresent();
            if (!spBelongsToSa) {
                result.getMessages().add("servicePointId must be a number");
                return;
            }
        }
    }

    private void validateAggregator(String selectedAggregator, ProgramAdapter result) {
        if (selectedAggregator == null) {
            return;
        }
        if (!NumberUtils.isNumber(selectedAggregator)) {
            result.getMessages().add("Aggregator must be a number");
            return;
        }
        final ProgramAggregator aggregator = aggregatorService.getById(Long.parseLong(selectedAggregator));
        if (aggregator == null) {
            result.getMessages().add("Aggregator not found");
            return;
        }
    }

    private void validateFsl(String fsl, ProgramAdapter result) {
        if (!NumberUtils.isNumber(fsl)) {
            result.getMessages().add("Firm Service Level must be Integer");
        }
    }


    @Transactional("mysqlTransactionManager")
    public String getProgramById(String email, String programId) throws JsonProcessingException {
        if (NumberUtils.isNumber(programId)) {
            Long id = Long.valueOf(programId);
            Program program = programDao.getById(id);
            if (program != null) {
                final ProgramAdapter result = ProgramAdapter.build(program);
                return objectMapper.writeValueAsString(result);
            }
        }
        return null;
    }

    @Transactional("mysqlTransactionManager")
    public String viewEnroll(String user, String programId, String spId) throws JsonProcessingException {
        final EligibilityAdapter result = preEnroll(user, programId, spId);
        return objectMapper.writeValueAsString(result);
    }

    private EligibilityAdapter preEnroll(String user, String programId, String spId) {
        final EligibilityAdapter result = new EligibilityAdapter();
        validateUser(user, result);
        validateProgram(programId, result);
        if (!result.hasError()) {
            EligibilityEngine eligibilityEngine = new EligibilityEngine();
            List<ServiceAgreementEligibility> potentialServiceAgreements = new ArrayList<>();
            final ArrayList<String> messages = new ArrayList();
            try {
                final List<AggregatorAdapter> aggregators = aggregatorService.getAll();
                result.setPeakDemandResponse(eligibilityEngine.verify(result.getServiceAgreement().getServiceAgreementId(), agreementPointMapService, peakDemandService, dataMappingService, potentialServiceAgreements, result.getActiveProfile(), result.getProgram(), messages));
                AgreementPointMapPK pk = new AgreementPointMapPK(spId, result.getServiceAgreement().getServiceAgreementId());
                result.setServicePointsEligibility(potentialServiceAgreements.stream().filter(psa -> psa.getAgreementPointMap().getId().equals(pk))
                        .map(eligibility -> {
                            final ServiceAgreementEligibilityAdapter eligibilityAdapter = ServiceAgreementEligibilityAdapter.build(eligibility);
                            eligibilityAdapter.setFslRange(enrollmentEngine.viewEnroll(eligibility, result.getActiveProfile(), result.getPeakDemandResponse()));
                            eligibilityAdapter.setAggregators(aggregators);
                            eligibilityAdapter.setEnrollmentAttributeAdapters(Arrays.stream(ConstantsProviderModel.ENROLLMENT_ATTRIBUTES)
                                    .filter(attribute ->
                                            result.getActiveProfile().isAttributeInTheProgramEnrollmentAttributeList(attribute.getId()))
                                    .map(attribute -> new EnrollmentAttributeAdapter(attribute.getId(), attribute.getLabel())).collect(Collectors.toList()));
                            return eligibilityAdapter;
                        }).collect(Collectors.toList()));
            } catch (BusinessException e) {
                log.error(e.getBusinessMessage());
                result.getMessages().add(e.getBusinessMessage());
            }
            if (CollectionUtils.isNotEmpty(messages)) {
                result.getMessages().addAll(messages);
            }
        }
        return result;
    }
}
