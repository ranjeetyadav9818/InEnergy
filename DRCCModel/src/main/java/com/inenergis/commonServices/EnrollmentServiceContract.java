package com.inenergis.commonServices;

import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;

import java.util.List;

/**
 * Created by egamas on 15/10/2017.
 */
public interface EnrollmentServiceContract {
    List<ProgramServiceAgreementEnrollment> getBySaIdsAndProgram(List<String> ids, EnrolmentStatus status, Program program);

    List<ProgramServiceAgreementEnrollment> getBySaIdStatusesAndProgram(String id, List<EnrolmentStatus> statuses, Program program);

    void saveOrUpdate(ProgramServiceAgreementEnrollment saEnrollment);

    void saveOrUpdateAttribute(ProgramServiceAgreementEnrollmentAttribute attribute);
}
