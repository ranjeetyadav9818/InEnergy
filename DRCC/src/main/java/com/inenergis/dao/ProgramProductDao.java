package com.inenergis.dao;

import com.inenergis.entity.program.ProgramProduct;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
@Getter
@Setter
public class ProgramProductDao extends GenericDao<ProgramProduct> {

    public ProgramProductDao() {
        setClazz(ProgramProduct.class);
    }
}
