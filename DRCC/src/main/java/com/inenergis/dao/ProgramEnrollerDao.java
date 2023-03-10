package com.inenergis.dao;

import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramProfile;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ProgramEnrollerDao extends GenericDao<ProgramEnroller>  {

    public ProgramEnrollerDao(){
        setClazz(ProgramEnroller.class);
    }
}
