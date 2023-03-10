package com.inenergis.dao;

import com.inenergis.entity.program.ProgramSeason;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ProgramSeasonDao extends GenericDao<ProgramSeason> {

    public ProgramSeasonDao() {
        setClazz(ProgramSeason.class);
    }
}
