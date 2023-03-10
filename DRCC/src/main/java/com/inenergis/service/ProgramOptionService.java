package com.inenergis.service;

import com.inenergis.dao.ProgramOptionDao;
import com.inenergis.entity.program.ProgramOption;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Setter
public class ProgramOptionService {

    private static final Logger log = LoggerFactory.getLogger(ProgramOptionService.class);

    @Inject
    ProgramOptionDao programOptionDao;

    public ProgramOption getById(Long id) {
        return programOptionDao.getById(id);
    }
}