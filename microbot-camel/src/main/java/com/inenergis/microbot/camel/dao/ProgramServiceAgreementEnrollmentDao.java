package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface ProgramServiceAgreementEnrollmentDao extends Repository<ProgramServiceAgreementEnrollment, Long> {

    ProgramServiceAgreementEnrollment save(ProgramServiceAgreementEnrollment programServiceAgreementEnrollment);

    ProgramServiceAgreementEnrollment getFirstByProgramAndServiceAgreementAndEffectiveStartDateGreaterThanEqualAndEffectiveEndDateLessThan(Program program, ServiceAgreement serviceAgreement, Date dateOne, Date dateTwo);

    ProgramServiceAgreementEnrollment getById(Long id);

    List<ProgramServiceAgreementEnrollment> findAllByEnrollmentStatus(String status);
}