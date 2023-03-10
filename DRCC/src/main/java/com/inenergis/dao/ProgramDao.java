package com.inenergis.dao;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.program.Program;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class ProgramDao extends GenericDao<Program>  {

    public ProgramDao(){
        setClazz(Program.class);
    }
}
