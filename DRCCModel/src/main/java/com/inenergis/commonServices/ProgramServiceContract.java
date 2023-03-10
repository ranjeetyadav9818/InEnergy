package com.inenergis.commonServices;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import com.inenergis.exception.BusinessException;

import javax.jms.JMSException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * Created by egamas on 13/10/2017.
 */
public interface ProgramServiceContract {
    @Transactional
    Program saveProgram(Program program) throws IOException;

    @Transactional
    void saveProgramAndProfile(Program program, ProgramProfile profile, String email) throws IOException;
    @Transactional
    List<Program> getPrograms();
    @Transactional
    Program getProgram(Long programId);
    @Transactional
    ProgramProfile getProfile(Long id);
    @Transactional
    void deleteProgramEnroller(ProgramEnroller enroller);

    @Transactional
    void saveProgramEnrollment(ProgramServiceAgreementEnrollment saEnrollment, AgreementPointMap agreementPointMap, ProgramProfile currentProfile, JMSUtilContract jmsUtil) throws BusinessException, JMSException;
    @Transactional
    void initSafetyReductionFactors(ProgramProfile currentProfile);
    @Transactional
    void updateApplicationEnrollment(ProgramServiceAgreementEnrollment currentProgramEnrolled, String author) throws JMSException, BusinessException;
    @Transactional
    Long getProgramNumberOfActiveSAs(Program program);
    @Transactional
    List<ProgramServiceAgreementEnrollment> getActiveSAs(Program program);
    @Transactional
    ProgramServiceAgreementEnrollment getServiceAgreementEnrollment(Long applicationId);
    @Transactional
    List<ProgramServiceAgreementEnrollmentAttribute> getAttributesFromEnrollment(ProgramServiceAgreementEnrollment applicationEnrollment);
    @Transactional
    List<Program> getAllWithActiveProfile();
    @Transactional
    List<ProgramProfile> getOverlappedProfiles(ProgramProfile profile);
}
