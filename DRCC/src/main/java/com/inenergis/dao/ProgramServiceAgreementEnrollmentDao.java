package com.inenergis.dao;

import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ProgramServiceAgreementEnrollmentDao extends GenericDao<ProgramServiceAgreementEnrollment> {

    public ProgramServiceAgreementEnrollmentDao() {
        setClazz(ProgramServiceAgreementEnrollment.class);
    }
}
