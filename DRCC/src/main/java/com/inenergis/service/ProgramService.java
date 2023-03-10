package com.inenergis.service;

import com.inenergis.commonServices.EnrollmentEngine;
import com.inenergis.commonServices.EnrollmentServiceContract;
import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.EligibleProgramsDao;
import com.inenergis.dao.NoComparisonCheck;
import com.inenergis.dao.ProgramDao;
import com.inenergis.dao.ProgramEnrollerDao;
import com.inenergis.dao.ProgramEnrollmentAttributeDao;
import com.inenergis.dao.ProgramProfileDao;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentAttributeDao;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.DRCCBeanUtils;
import com.inenergis.entity.History;
import com.inenergis.entity.bidding.SafetyReductionFactorHe;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramSeason;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import com.inenergis.exception.BusinessException;
import com.inenergis.model.ElasticProgramConverter;
import com.inenergis.util.ElasticActionsUtil;
import com.inenergis.util.ElasticConnectionPool;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.model.ElasticProgram.ELASTIC_TYPE;
import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
@Setter
public class ProgramService implements ProgramServiceContract {

    private static final Logger log = LoggerFactory.getLogger(ProgramService.class);

    @Inject
    HistoryService historyService;

    @Inject
    ProgramDao programDao;

    @Inject
    EligibleProgramsDao eligibleProgramsDao;

    @Inject
    ProgramProfileDao programProfileDao;

    @Inject
    ProgramEnrollerDao programEnrollerDao;

    @Inject
    ProgramEnrollmentAttributeDao programEnrollmentAttributeDao;

    @Inject
    ProgramServiceAgreementEnrollmentDao programServiceAgreementEnrollmentDao;

    @Inject
    EnrollmentServiceContract enrollmentService;

    @Inject
    ProgramServiceAgreementEnrollmentAttributeDao programServiceAgreementEnrollmentAttributeDao;

    @Inject
    WorkPlanServiceContract workflowService;

    @Inject
    MailService mailService;

    @Inject
    ElasticConnectionPool elasticConnectionPool;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    @Inject
    JMSUtilContract jmsUtil;

    EnrollmentEngine enrollmentEngine = new EnrollmentEngine();
    private Boolean elasticCheck=false;

    @Override
    @Transactional
    public Program saveProgram(Program program) throws IOException {
        Program update = programDao.saveOrUpdate(program);
        if(elasticCheck == true) {
            elasticActionsUtil.indexDocument(update.getId().toString(), ElasticProgramConverter.convert(program), ENERGY_ARRAY_INDEX, ELASTIC_TYPE);
        }
        return update;
    }

    @Override
    @Transactional
    public void saveProgramAndProfile(Program program, ProgramProfile profile, String email) throws IOException {
        Program updatedProgram = programDao.saveOrUpdate(program);
        Program previousProgram = profile.getProgram();
        profile.setProgram(updatedProgram);
        ProgramProfile updatedProfile = saveProfile(profile, email, previousProgram);
        updatedProgram.getProfiles().add(updatedProfile);
        if (elasticCheck == true) {
            elasticActionsUtil.indexDocument(updatedProgram.getId().toString(), ElasticProgramConverter.convert(updatedProgram), ENERGY_ARRAY_INDEX, ELASTIC_TYPE);
        }
    }

    @Override
    public List<Program> getPrograms() {
        return programDao.getAll();
    }

    @Override
    public Program getProgram(Long programId) {
        return programDao.getById(programId);
    }


    private ProgramProfile saveProfile(ProgramProfile profile, String author, Program previousProgram) {
        //This should be called automatically but it is not;
        profile.onUpdate();

        if (profile.getSeasons() != null) {
            for (ProgramSeason programSeason : profile.getSeasons()) {
                programSeason.onCreate();
            }
        }

        if (profile.getId() != null) {
            final ProgramProfile profileFromDatabase = getProfile(profile.getId());
            if (CollectionUtils.isNotEmpty(profileFromDatabase.getCustomerNotifications())) {//This is mandatory, if it's null it's because this is the first edition, no history should be generated
                try {
                    final List<History> histories = DRCCBeanUtils.generateHistoryFromDifferences(profileFromDatabase, profile, null, Arrays.asList("program", "lastUpdate"), "id", author, true);

                    historyService.saveHistory(histories);
                } catch (Exception e) {
                    log.error("exception generating/saving historical changes in profile", e);
                }
            }
            try {
                List<History> programHistories = DRCCBeanUtils.generateHistoryFromDifferences(previousProgram, profile.getProgram(), null, null, "id", author, false);
                for (History history : programHistories) {
                    history.setEntity(ProgramProfile.class.getSimpleName());
                    history.setEntityId(profileFromDatabase.getId().toString());
                }
                historyService.saveHistory(programHistories);
            } catch (Exception e) {
                log.error("exception generating/saving historical changes in profile", e);
            }
        }
        return programProfileDao.saveOrUpdate(profile);
    }

    @Override
    public ProgramProfile getProfile(Long id) {
        return programProfileDao.getById(id);
    }

    @Override
    public void deleteProgramEnroller(ProgramEnroller enroller) {
        programEnrollerDao.delete(enroller);
    }

    @Override
    @Transactional
    public void saveProgramEnrollment(ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap agreementPointMap, ProgramProfile currentProfile, JMSUtilContract jmsUtil) throws BusinessException, JMSException {
        enrollmentEngine.saveProgramEnrollment( saEnrollment,  agreementPointMap, currentProfile, enrollmentService, workflowService, jmsUtil );

    }

    @Override
    public void initSafetyReductionFactors(ProgramProfile currentProfile) {
        if ((CollectionUtils.isEmpty(currentProfile.getSafetyReductionFactors()))) {
            ArrayList<SafetyReductionFactorHe> safetyReductionFactors = new ArrayList<>();
            for (HourEnd hourEnd : HourEnd.values()) {
                SafetyReductionFactorHe safetyReductionFactorHe = new SafetyReductionFactorHe();
                safetyReductionFactorHe.setProfile(currentProfile);
                safetyReductionFactorHe.setHourEnd(hourEnd);
                safetyReductionFactorHe.setMonday(0L);
                safetyReductionFactorHe.setTuesday(0L);
                safetyReductionFactorHe.setWednesday(0L);
                safetyReductionFactorHe.setThursday(0L);
                safetyReductionFactorHe.setFriday(0L);
                safetyReductionFactorHe.setSaturday(0L);
                safetyReductionFactorHe.setSunday(0L);
                safetyReductionFactorHe.setProgramHoliday(0L);
                safetyReductionFactors.add(safetyReductionFactorHe);
            }
            currentProfile.setSafetyReductionFactors(safetyReductionFactors);
        }
    }



    @Override
    public void updateApplicationEnrollment(ProgramServiceAgreementEnrollment currentProgramEnrolled, String author) throws JMSException, BusinessException {
        if (currentProgramEnrolled.getId() != null) {
            ProgramServiceAgreementEnrollment enrollmentFromDB = getServiceAgreementEnrollment(currentProgramEnrolled.getId());
            try {
                final List<History> histories = DRCCBeanUtils.generateHistoryFromDifferences(enrollmentFromDB, currentProgramEnrolled, null,
                        Arrays.asList("uuid", "snapshots", "lastFSL", "program", "serviceAgreement", "locations", "lastLocation"), "id", author, true);
                historyService.saveHistory(histories);
            } catch (Exception e) {
                log.error("exception generating/saving historical changes in profile", e);
            }
            if (isChangeAnUnerollment(currentProgramEnrolled, enrollmentFromDB)) {
                workflowService.createProgramUnenrollmentPlan(enrollmentFromDB, jmsUtil);
            }
        }
        programServiceAgreementEnrollmentDao.saveOrUpdate(currentProgramEnrolled);
    }

    private boolean isChangeAnUnerollment(ProgramServiceAgreementEnrollment currentProgramEnrolled, ProgramServiceAgreementEnrollment enrollmentFromDB) {
        if (!currentProgramEnrolled.getEnrollmentStatus().equals(enrollmentFromDB.getEnrollmentStatus())) {
            return currentProgramEnrolled.getEnrollmentStatus().equals(EnrolmentStatus.UNENROLLED.getName()) || currentProgramEnrolled.getEnrollmentStatus().equals(EnrolmentStatus.CANCELLED.getName());
        }
        return false;
    }

    @Override
    public Long getProgramNumberOfActiveSAs(Program program) {
        return programServiceAgreementEnrollmentDao.countWithCriteria(getSAsConditions(program, true));
    }

    @Override
    public List<ProgramServiceAgreementEnrollment> getActiveSAs(Program program) {
        return programServiceAgreementEnrollmentDao.getWithCriteria(getSAsConditions(program, true));
    }

    private List<CriteriaCondition> getSAsConditions(Program program, boolean activeOnly) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        if (activeOnly) {
            conditions.add(CriteriaCondition.builder().key("effectiveEndDate").noComparisonCheck(NoComparisonCheck.NULL).build());
            conditions.add(CriteriaCondition.builder().key("enrollmentStatus").value(EnrolmentStatus.ENROLLED.getName()).matchMode(MatchMode.EXACT).build());
        }
        conditions.add(CriteriaCondition.builder().key("program").value(program).matchMode(MatchMode.EXACT).build());

        return conditions;
    }

    @Override
    public ProgramServiceAgreementEnrollment getServiceAgreementEnrollment(Long applicationId) {
        return programServiceAgreementEnrollmentDao.getById(applicationId);
    }

    @Override
    public List<ProgramServiceAgreementEnrollmentAttribute> getAttributesFromEnrollment(ProgramServiceAgreementEnrollment applicationEnrollment) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        CriteriaCondition condition = CriteriaCondition.builder().key("enrollment").value(applicationEnrollment).matchMode(MatchMode.EXACT).build();
        conditions.add(condition);
        return programServiceAgreementEnrollmentAttributeDao.getWithCriteria(conditions);
    }

    @Override
    public List<Program> getAllWithActiveProfile() {
        return getPrograms().stream()
                .filter(program -> program.getActiveProfile() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgramProfile> getOverlappedProfiles(ProgramProfile profile) {
        return programProfileDao.getOverlapped(profile);
    }

}