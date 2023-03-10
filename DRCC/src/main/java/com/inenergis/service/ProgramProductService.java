package com.inenergis.service;

import com.inenergis.dao.ProgramProductDao;
import com.inenergis.entity.program.ProgramProduct;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ProgramProductService {
    @Inject
    ProgramProductDao productDao;

    public ProgramProduct getById(Long id){
        return productDao.getById(id);
    }

}
