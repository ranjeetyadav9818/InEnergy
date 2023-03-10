package com.inenergis.dao;

import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 04/10/2017.
 */
@Component
public interface ProgramServiceAgreementEnrollmentDao extends Repository<ProgramServiceAgreementEnrollment, Long> {

    @Transactional("mysqlTransactionManager")
    List<ProgramServiceAgreementEnrollment> getAllByProgramAndEnrollmentStatusAndEffectiveEndDateIsNull(Program program, String enrolmentStatus);

    @Transactional("mysqlTransactionManager")
    Long countAllByProgramAndEnrollmentStatusAndEffectiveEndDateIsNull(Program program, String enrolmentStatus);

    @Transactional("mysqlTransactionManager")
    List<ProgramServiceAgreementEnrollment> getAllByProgram(Program program);

    @Transactional("mysqlTransactionManager")
    List<ProgramServiceAgreementEnrollment> getBy(List<ServiceAgreement> ids, EnrolmentStatus status, Program program);

    @Transactional("mysqlTransactionManager")
    List<ProgramServiceAgreementEnrollment> getByServiceAgreementAndProgramAndEnrollmentStatusIn(ServiceAgreement sa, Program program, List<String> statuses);

    @Transactional("mysqlTransactionManager")
    @Modifying
    void save(ProgramServiceAgreementEnrollment saEnrollment);
}
