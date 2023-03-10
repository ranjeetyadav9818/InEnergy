package com.inenergis.service;

import com.inenergis.commonServices.DataMappingServiceContract;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.DataMappingDao;
import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DataMappingService implements DataMappingServiceContract {

    @Inject
    DataMappingDao dataMappingDao;

    public void delete(DataMapping dataMapping) {
        dataMappingDao.delete(dataMapping);
    }

    public List<DataMapping> getAll() {
        return dataMappingDao.getAll();
    }

    public List<DataMapping> getByType(DataMappingType type) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("type").value(type).matchMode(MatchMode.EXACT).build());

        return dataMappingDao.getWithCriteria(conditions);
    }

    @Override
    public String getDestinationBy(DataMappingType type, String source) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("type").value(type).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("source").value(source).matchMode(MatchMode.EXACT).build());

        DataMapping dataMapping = dataMappingDao.getUniqueResultWithCriteria(conditions);
        if (dataMapping != null) {
            return dataMapping.getDestination();
        }

        return null;
    }

    public DataMapping getById(Long id) {
        return dataMappingDao.getById(id);
    }

    public void saveOrUpdate(DataMapping dataMapping) {
        dataMappingDao.saveOrUpdate(dataMapping);
    }
}