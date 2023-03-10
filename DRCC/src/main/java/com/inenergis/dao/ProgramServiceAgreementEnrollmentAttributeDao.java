package com.inenergis.dao;

import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ProgramServiceAgreementEnrollmentAttributeDao extends GenericDao<ProgramServiceAgreementEnrollmentAttribute>  {

    public ProgramServiceAgreementEnrollmentAttributeDao(){
        setClazz(ProgramServiceAgreementEnrollmentAttribute.class);
    }
}
