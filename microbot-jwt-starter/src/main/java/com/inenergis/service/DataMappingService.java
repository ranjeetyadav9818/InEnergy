package com.inenergis.service;

import com.inenergis.commonServices.DataMappingServiceContract;
import com.inenergis.dao.DataMappingDao;
import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by egamas on 12/10/2017.
 */
@Getter
@Setter
@Component
public class DataMappingService implements DataMappingServiceContract {

    @Autowired
    private DataMappingDao dataMappingDao;

    @Transactional("mysqlTransactionManager")
    public void delete(Long id) {
        dataMappingDao.deleteById(id);
    }

    @Transactional("mysqlTransactionManager")
    public List<DataMapping> getAll() {
        return dataMappingDao.findAll();
    }

    @Transactional("mysqlTransactionManager")
    public List<DataMapping> getByType(DataMappingType type) {
        return dataMappingDao.getByType(type);
    }

    @Transactional("mysqlTransactionManager")
    public String getDestinationBy(DataMappingType type, String source) {
        return dataMappingDao.getBySourceAndType(source, type);
    }

    @Override
    @Transactional("mysqlTransactionManager")
    public void delete(DataMapping dataMapping) {
        throw new NotImplementedException();
    }

    @Transactional("mysqlTransactionManager")
    public DataMapping getById(Long id) {
        return dataMappingDao.getById(id);
    }

    @Transactional("mysqlTransactionManager")
    @Modifying
    public void saveOrUpdate(DataMapping dataMapping) {
        dataMappingDao.save(dataMapping);
    }
}
