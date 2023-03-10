package com.inenergis.dao;

import com.inenergis.entity.program.ProgramAggregator;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 04/10/2017.
 */
public interface AggregatorDao extends Repository<ProgramAggregator,Long> {

    @Transactional("mysqlTransactionManager")
    List<ProgramAggregator> findAll();

    @Transactional("mysqlTransactionManager")
    ProgramAggregator getById(long id);
}
