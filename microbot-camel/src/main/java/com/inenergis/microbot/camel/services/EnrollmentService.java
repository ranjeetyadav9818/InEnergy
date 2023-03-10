package com.inenergis.microbot.camel.services;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationChangelog;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.microbot.camel.dao.LocationChangelogDao;
import com.inenergis.microbot.camel.dao.LocationSubmissionExceptionDao;
import com.inenergis.microbot.camel.dao.PlanInstanceDao;
import com.inenergis.microbot.camel.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.microbot.camel.dao.WorkPlanDao;
import com.inenergis.util.VelocityUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    public static final String ENROLLMENT = "enrollment";

    @Autowired
    private LocationChangelogDao locationChangelogDao;

    @Autowired
    private ProgramServiceAgreementEnrollmentDao programServiceAgreementEnrollmentDao;

    @Autowired
    private WorkPlanDao workPlanDao;

    @Autowired
    private PlanInstanceDao planInstanceDao;

    @Autowired
    private LocationSubmissionExceptionDao locationSubmissionExceptionDao;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    @Autowired
    JMSUtilContract jmsUtil;

    @Transactional
    public void getEnrollment(Exchange exchange) {
        Long id;
        if (exchange.getIn().getBody() instanceof Long) {
            id = (Long) exchange.getIn().getBody();
        } else {
            id = Long.valueOf((String) exchange.getIn().getBody());
        }
        ProgramServiceAgreementEnrollment enrollment = programServiceAgreementEnrollmentDao.getById(id);
        if (enrollment != null) {
            // below is a workaround to avoid LazyInitializationException - could not initialize proxy - no Session
            try {
                enrollment.getProgram().getActiveProfile().getWholesaleIsoProduct().getProfile().getIso().getActiveProfile();
            } catch (Exception e) {
            }
            enrollment.getServiceAgreement().getAgreementPointMaps().isEmpty();
            if (!enrollment.getLocations().isEmpty()) {
                enrollment.getLocations().forEach(l -> l.getRegistrations().isEmpty());
            }
            exchange.setProperty(ENROLLMENT, enrollment);
        }
    }

    public void unenrollByCDWChange(ProgramServiceAgreementEnrollment enrollment) {
        Date endDate = enrollment.getServiceAgreement().getEndDate();
        enrollment.setEffectiveEndDate(endDate != null ? endDate : new Date());
        enrollment.setUnenrollReason("CDW Attribute Change");
        enrollment.setEnrollmentStatus(EnrolmentStatus.UNENROLLED.getName());
    }

    @Transactional
    @Modifying
    public void triggerUnenrollmentWorkflow(Exchange exchange) throws IOException {
        ProgramServiceAgreementEnrollment enrollment = (ProgramServiceAgreementEnrollment) exchange.getIn().getBody();
        WorkPlan workPlan = getWorkPlan(enrollment.getProgram(), WorkPlanType.UNENR);
        if (workPlan != null) {
            PlanInstance planInstance = (new WorkflowEngine()).buildProgramPlanInstance(enrollment, workPlan, new VelocityUtil(), appProperties, jmsUtil);
            planInstanceDao.save(planInstance);
        }
        unenrollByCDWChange(enrollment);
    }

    @Transactional
    private WorkPlan getWorkPlan(Program program, WorkPlanType workPlanType) {
        return workPlanDao.findByProgramAndType(program, workPlanType);
    }

    @Transactional
    @Modifying
    public void createUnenrollmentChangelog(Exchange exchange) {
        ProgramServiceAgreementEnrollment enrollment = (ProgramServiceAgreementEnrollment) exchange.getProperty(ENROLLMENT);
        if (enrollment.getLastLocation() != null) {
            IsoResource activeResource = enrollment.getLastLocation().getActiveResource();
            if (activeResource != null) {
                LocationChangelog lc = locationChangelogDao.findByEffectiveDateAndTypeAndLocation(enrollment.getEffectiveEndDate(), LocationChangelog.LocationChangelogType.UNENROLLED, enrollment.getLastLocation());
                if (lc != null) {
                    return;
                }

                LocationChangelog changelog = new LocationChangelog();
                changelog.setCreationDate(new Date());
                changelog.setEffectiveDate(enrollment.getEffectiveEndDate());
                changelog.setUserId("System-triggered");
                changelog.setType(LocationChangelog.LocationChangelogType.UNENROLLED);
                changelog.setLocation(enrollment.getLastLocation());
                changelog.setIsoResource(activeResource);

                locationChangelogDao.save(changelog);
            }
        } else {
            exchange.getIn().setHeader("locationNotExist", true);
            enrollment.setEnrollmentStatus(EnrolmentStatus.CANCELLED.getName());
        }
    }

    @Transactional
    public void getEnrollmentsWithReviewedExceptions(Exchange exchange) {
        List<LocationSubmissionException> exceptions = locationSubmissionExceptionDao.getAllByMarkedRetryAndResolved(true, false);
        exchange.getIn().setBody(exceptions);
    }

    @Transactional
    @Modifying
    public void exceptionsForwarded(Exchange exchange) {
        LocationSubmissionException locationSubmissionException = (LocationSubmissionException) exchange.getProperty("exceptionProperty");
        locationSubmissionException.setResolved(true);
        locationSubmissionExceptionDao.save(locationSubmissionException);
    }

    @Transactional
    public void getReinstateEnrollments(Exchange exchange) {
        List<ProgramServiceAgreementEnrollment> result = programServiceAgreementEnrollmentDao.findAllByEnrollmentStatus(EnrolmentStatus.REINSTATE.getName());
        result.forEach(e -> e.getLastLocation().getIso().getActiveProfile());
        result.forEach(e -> e.getServiceAgreement().getAgreementPointMaps().isEmpty());
        exchange.getIn().setBody(result);
    }
}