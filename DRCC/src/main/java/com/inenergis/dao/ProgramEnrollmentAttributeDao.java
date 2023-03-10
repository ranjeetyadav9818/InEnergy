package com.inenergis.dao;

import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramEnrollmentAttribute;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ProgramEnrollmentAttributeDao extends GenericDao<ProgramEnrollmentAttribute>  {

    public ProgramEnrollmentAttributeDao(){
        setClazz(ProgramEnrollmentAttribute.class);
    }
}
