package com.inenergis.service;

import com.inenergis.commonServices.EnrollmentServiceContract;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentAttributeDao;
import com.inenergis.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EnrollmentService implements EnrollmentServiceContract {

    @Inject
    ProgramServiceAgreementEnrollmentDao programServiceAgreementEnrollmentDao;

    @Inject
    ProgramServiceAgreementEnrollmentAttributeDao programServiceAgreementEnrollmentAttributeDao;

    @Override
    public List<ProgramServiceAgreementEnrollment> getBySaIdsAndProgram(List<String> ids, EnrolmentStatus status, Program program) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("enrollmentStatus").value(status.getName()).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("serviceAgreement.serviceAgreementId").value(ids).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("program.name").value(program.getName()).matchMode(MatchMode.EXACT).build());
        return programServiceAgreementEnrollmentDao.getWithCriteria(conditions);
    }

    @Override
    public List<ProgramServiceAgreementEnrollment> getBySaIdStatusesAndProgram(String id, List<EnrolmentStatus> statuses, Program program) {
        List<String> statusNames = statuses.stream().map(EnrolmentStatus::getName).collect(Collectors.toList());
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("enrollmentStatus").value(statusNames).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("serviceAgreement.serviceAgreementId").value(id).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("program.name").value(program.getName()).matchMode(MatchMode.EXACT).build());
        return programServiceAgreementEnrollmentDao.getWithCriteria(conditions);
    }

    @Override
    public void saveOrUpdate(ProgramServiceAgreementEnrollment saEnrollment) {
        programServiceAgreementEnrollmentDao.saveOrUpdate(saEnrollment);
    }

    @Override
    public void saveOrUpdateAttribute(ProgramServiceAgreementEnrollmentAttribute attribute) {
        programServiceAgreementEnrollmentAttributeDao.save(attribute);
    }
}
