package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.EnergyContractDao;
import com.inenergis.entity.genericEnum.RelatedContractType;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.model.ElasticContractConverter;
import com.inenergis.util.ElasticActionsUtil;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.inenergis.model.ElasticContract.ELASTIC_TYPE;
import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
public class EnergyContractService {

    @Inject
    EnergyContractDao energyContractDao;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    @Transactional
    public EnergyContract saveOrUpdate(EnergyContract energyContract) throws IOException {
        final EnergyContract energyContractSaved = energyContractDao.saveOrUpdate(energyContract);
        elasticActionsUtil.indexDocument(energyContract.getId().toString(), ElasticContractConverter.convert(energyContract), ENERGY_ARRAY_INDEX, ELASTIC_TYPE);
        return energyContractSaved;

    }

    public List<EnergyContract> getAll() {
        return energyContractDao.getAll();
    }

    public EnergyContract getById(Long id) {
        return energyContractDao.getById(id);
    }

    public List<EnergyContract> findByTypeName(RelatedContractType type, String name) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("type").value(type).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("name").value(name).matchMode(MatchMode.START).build());
        return energyContractDao.getWithCriteria(conditions);
    }
}