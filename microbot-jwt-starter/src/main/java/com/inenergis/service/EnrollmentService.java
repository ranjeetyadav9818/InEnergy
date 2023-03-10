package com.inenergis.service;

import com.inenergis.commonServices.EnrollmentServiceContract;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentAttributeDao;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 15/10/2017.
 */
@Component
public class EnrollmentService implements EnrollmentServiceContract {
    @Autowired
    ProgramServiceAgreementEnrollmentDao enrollmentDao;

    @Autowired
    ProgramServiceAgreementEnrollmentAttributeDao programServiceAgreementEnrollmentAttributeDao;

    @Override
    @Transactional("mysqlTransactionManager")
    public List<ProgramServiceAgreementEnrollment> getBySaIdsAndProgram(List<String> ids, EnrolmentStatus status, Program program) {
        final List<ServiceAgreement> saIds = ids.stream().map(id -> {
            ServiceAgreement sa = new ServiceAgreement();
            sa.setServiceAgreementId(id);
            return sa;
        })
                .collect(Collectors.toList());
        return enrollmentDao.getBy(saIds,status ,program);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public List<ProgramServiceAgreementEnrollment> getBySaIdStatusesAndProgram(String id, List<EnrolmentStatus> statuses, Program program) {
        ServiceAgreement sa = new ServiceAgreement();
        sa.setServiceAgreementId(id);
        final List<String> statusesAsStr = statuses.stream().map(s -> s.getName()).collect(Collectors.toList());
        return enrollmentDao.getByServiceAgreementAndProgramAndEnrollmentStatusIn(sa,program,statusesAsStr);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void saveOrUpdate(ProgramServiceAgreementEnrollment saEnrollment) {
        enrollmentDao.save(saEnrollment);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    @Modifying
    public void saveOrUpdateAttribute(ProgramServiceAgreementEnrollmentAttribute attribute) {
        programServiceAgreementEnrollmentAttributeDao.save(attribute);
    }
}
