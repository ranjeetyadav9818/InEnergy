package com.inenergis.dao;

import com.inenergis.entity.program.ProgramOption;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ProgramOptionDao extends GenericDao<ProgramOption> {

    public ProgramOptionDao() {
        setClazz(ProgramOption.class);
    }
}

