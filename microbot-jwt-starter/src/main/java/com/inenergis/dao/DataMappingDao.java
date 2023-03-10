package com.inenergis.dao;

import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 09/10/2017.
 */
@Component
public interface DataMappingDao extends Repository<DataMapping, Long> {

    @Transactional("mysqlTransactionManager")
    @Modifying
    void deleteById(Long id);

    @Transactional("mysqlTransactionManager")
    List<DataMapping> findAll();

    @Transactional("mysqlTransactionManager")
    List<DataMapping> getByType(DataMappingType type);

    @Transactional("mysqlTransactionManager")
    String getBySourceAndType( String source, DataMappingType type);

    @Transactional("mysqlTransactionManager")
    DataMapping getById(Long id);

    @Transactional("mysqlTransactionManager")
    @Modifying
    void save(DataMapping dataMapping);
}
