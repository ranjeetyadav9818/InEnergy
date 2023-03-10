package com.inenergis.service;

import com.inenergis.dao.ProgramSeasonDao;
import com.inenergis.entity.program.ProgramSeason;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ProgramSeasonService {

    @Inject
    private ProgramSeasonDao programSeasonDao;

    public ProgramSeason getById(Long id) {
        return programSeasonDao.getById(id);
    }


    public ProgramSeason saveOrUpdate(ProgramSeason programSeason) {
        return programSeasonDao.saveOrUpdate(programSeason);
    }
}