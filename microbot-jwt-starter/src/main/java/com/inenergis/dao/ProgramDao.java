package com.inenergis.dao;

import com.inenergis.entity.program.Program;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 09/10/2017.
 */
@Component
public interface ProgramDao extends Repository<Program,Long>{
    @Transactional("mysqlTransactionManager")
    List<Program> getProgramsByActiveEquals(boolean active);
    @Transactional("mysqlTransactionManager")
    Program getById(Long id);
}
