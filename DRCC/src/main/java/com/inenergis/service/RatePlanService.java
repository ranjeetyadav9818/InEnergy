package com.inenergis.service;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.controller.program.RateBulkUploadController;
import com.inenergis.dao.ContractTypeDao;
import com.inenergis.dao.RatePlanDao;
import com.inenergis.dao.RatePlanEnrollmentDao;
import com.inenergis.dao.RatePlanProfileDao;
import com.inenergis.entity.ContractType;
import com.inenergis.entity.DRCCBeanUtils;
import com.inenergis.entity.History;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.program.ApplicableRatePlan;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Stateless
public class RatePlanService {

    @Inject
    private RatePlanDao ratePlanDao;
    @Inject
    private ContractTypeDao contractTypeDao;
    @Inject
    private RatePlanProfileDao ratePlanProfileDao;
    @Inject
    private RatePlanEnrollmentDao ratePlanEnrollmentDao;
    @Inject
    private WorkPlanServiceContract workPlanService;
    @Inject
    HistoryService historyService;
    @Inject
    JMSUtilContract jmsUtil;

    private static final Logger log = LoggerFactory.getLogger(RatePlanService.class);


    public List<RatePlan> getAll() {
        return ratePlanDao.getAll();
    }

    public RatePlan getById(Long id) {
        return ratePlanDao.getById(id);
    }

    public RatePlanProfile getProfileById(Long id) {
        return ratePlanProfileDao.getById(id);
    }

    public RatePlan saveOrUpdate(RatePlan ratePlan) {
        return ratePlanDao.saveOrUpdate(ratePlan);
    }

    @Transactional
    public RatePlanProfile saveOrUpdateProfile(RatePlanProfile newProfile, String author) {
        newProfile.onUpdate();
        generateHistory(newProfile, author);
        return ratePlanProfileDao.saveOrUpdate(newProfile);
    }

    private void generateHistory(RatePlanProfile profile, String author) {
        if (profile.getId() != null) {
            final RatePlanProfile profileFromDatabase = getProfileById(profile.getId());
            try {
                final List<History> histories = DRCCBeanUtils.generateHistoryFromDifferences(profileFromDatabase, profile, null, Arrays.asList("ratePlan", "lastUpdate", "effectiveStartDate"), "id", author, true);
                historyService.saveHistory(histories);
            } catch (Exception e) {
                log.error("exception generating/saving historical changes in profile", e);
            }
        }
    }


    public RatePlanEnrollment saveOrUpdate(RatePlanEnrollment enrollment) {
        return ratePlanEnrollmentDao.saveOrUpdate(enrollment);
    }

    @Transactional
    public void saveOrUpdate(List<RatePlanEnrollment> enrollments) {
        for (RatePlanEnrollment enrollment : enrollments) {
            saveOrUpdate(enrollment);
        }
    }

    @Transactional
    public List<RatePlanEnrollment> enroll(RatePlan ratePlan, BaseServiceAgreement serviceAgreement) throws BusinessException {
        List<RatePlanEnrollment> enrollments = new ArrayList<>();
        enrollments.add(generateEnrollment(ratePlan, serviceAgreement));
        if (CollectionUtils.isNotEmpty(ratePlan.getActiveProfile().getApplicableRatePlans())) {
            for (ApplicableRatePlan applicableRatePlan : ratePlan.getActiveProfile().getApplicableRatePlans()) {
                if (findActiveRatePlanEnrollmentInServiceAgreement(serviceAgreement, applicableRatePlan.getRatePlan()) == null) {
                    enrollments.add(generateEnrollment(applicableRatePlan.getRatePlan(), serviceAgreement));
                }
            }
        }
        for (RatePlanEnrollment enrollment : enrollments) {
            workPlanService.createRatePlanEnrollmentPlan(enrollment, jmsUtil);
            saveOrUpdate(enrollment);
        }
        return enrollments;
    }


    protected RatePlanEnrollment generateEnrollment(RatePlan ratePlan, BaseServiceAgreement serviceAgreement) {
        RatePlanEnrollment enrollment = new RatePlanEnrollment();
        enrollment.setServiceAgreement(serviceAgreement);
        enrollment.setRatePlan(ratePlan);
        enrollment.setStatus(RatePlanEnrollment.ENROLLED);
        enrollment.setStartDate(new Date());
        return enrollment;
    }

    public RatePlanEnrollment findActiveRatePlanEnrollmentInServiceAgreement(BaseServiceAgreement serviceAgreement, RatePlan rate) {
        for (RatePlanEnrollment ratePlanEnrollment : serviceAgreement.getRatePlanEnrollments()) {
            if (ratePlanEnrollment.isActive() && ratePlanEnrollment.getRatePlan().equals(rate)) {
                return (RatePlanEnrollment) ratePlanEnrollment;
            }
        }
        return null;
    }

    public void unenroll(RatePlanEnrollment enrollment) throws BusinessException {
        enrollment.setEndDate(new Date());
        enrollment.setStatus(RatePlanEnrollment.UNENROLLED);
        workPlanService.createRatePlanUnenrollmentPlan(enrollment, jmsUtil);
        saveOrUpdate(enrollment);
    }
    public List<ContractType> getContractTypes() {
        return contractTypeDao.getAll();
    }

    public ContractType getContractType(Long contractTypeId) {
        return contractTypeDao.getById(contractTypeId);
    }
    @Transactional
    public List<RatePlanEnrollment> unenrollAndRelated(RatePlanEnrollment enrollment, BaseServiceAgreement serviceAgreement) throws BusinessException {
        enrollment.setEndDate(new Date());
        enrollment.setStatus(RatePlanEnrollment.UNENROLLED);
        List<RatePlanEnrollment> unenrollments = new ArrayList<>();
        unenrollments.add(enrollment);
        for (ApplicableRatePlan applicableRatePlan : enrollment.getRatePlan().getActiveProfile().getApplicableRatePlans()) {
            RatePlanEnrollment enrollmentInServiceAgreement = findActiveRatePlanEnrollmentInServiceAgreement(serviceAgreement, applicableRatePlan.getRatePlan());
            if (enrollmentInServiceAgreement != null) {
                enrollmentInServiceAgreement.setEndDate(new Date());
                enrollmentInServiceAgreement.setStatus(RatePlanEnrollment.UNENROLLED);
                unenrollments.add(enrollmentInServiceAgreement);
            }
        }
        for (RatePlanEnrollment unenrollment : unenrollments) {
            workPlanService.createRatePlanUnenrollmentPlan(unenrollment, jmsUtil);
        }
        saveOrUpdate(unenrollments);
        return unenrollments;
    }

    @Transactional
    public void enroll(List<RateBulkUploadController.BulkEnrollment> enrollments) throws BusinessException {
        for (RateBulkUploadController.BulkEnrollment enrollment : enrollments) {
            enroll(enrollment.getRatePlan(), enrollment.getServiceAgreement());
        }
    }
}