package com.inenergis.dao;

import com.inenergis.entity.program.EligibleProgram;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class EligibleProgramsDao extends GenericDao<EligibleProgram> {

    public EligibleProgramsDao() {
        setClazz(EligibleProgram.class);
    }
}

