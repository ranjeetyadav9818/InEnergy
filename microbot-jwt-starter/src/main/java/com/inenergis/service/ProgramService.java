package com.inenergis.service;

import com.inenergis.commonServices.EnrollmentEngine;
import com.inenergis.commonServices.EnrollmentServiceContract;
import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import com.inenergis.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.List;

/**
 * Created by egamas on 14/10/2017.
 */
@Component
public class ProgramService implements ProgramServiceContract {

    @Autowired
    EnrollmentServiceContract enrollmentService;

    @Autowired
    WorkPlanServiceContract workflowService;

    @Autowired
    ProgramServiceAgreementEnrollmentDao programServiceAgreementEnrollmentDao;

    EnrollmentEngine enrollmentEngine =  new EnrollmentEngine();

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public Program saveProgram(Program program) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void saveProgramAndProfile(Program program, ProgramProfile profile, String email) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<Program> getPrograms() {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public Program getProgram(Long programId) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public ProgramProfile getProfile(Long id) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void deleteProgramEnroller(ProgramEnroller enroller) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void saveProgramEnrollment(ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap agreementPointMap, ProgramProfile currentProfile, JMSUtilContract jmsUtil) throws BusinessException, JMSException {
        enrollmentEngine.saveProgramEnrollment( saEnrollment,  agreementPointMap, currentProfile, enrollmentService, workflowService , jmsUtil);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public void initSafetyReductionFactors(ProgramProfile currentProfile) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void updateApplicationEnrollment(ProgramServiceAgreementEnrollment currentProgramEnrolled, String author) throws JMSException, BusinessException {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public Long getProgramNumberOfActiveSAs(Program program) {
        return programServiceAgreementEnrollmentDao.countAllByProgramAndEnrollmentStatusAndEffectiveEndDateIsNull(program, EnrolmentStatus.ENROLLED.getName());
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<ProgramServiceAgreementEnrollment> getActiveSAs(Program program) {
        return programServiceAgreementEnrollmentDao.getAllByProgramAndEnrollmentStatusAndEffectiveEndDateIsNull(program, EnrolmentStatus.ENROLLED.getName());
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public ProgramServiceAgreementEnrollment getServiceAgreementEnrollment(Long applicationId) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<ProgramServiceAgreementEnrollmentAttribute> getAttributesFromEnrollment(ProgramServiceAgreementEnrollment applicationEnrollment) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<Program> getAllWithActiveProfile() {
        throw new NotImplementedException();
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<ProgramProfile> getOverlappedProfiles(ProgramProfile profile) {
        throw new NotImplementedException();
    }
}
