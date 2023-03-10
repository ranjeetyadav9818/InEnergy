package com.inenergis.service;

import com.inenergis.dao.ContractEntityDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.model.ElasticContractEntity;
import com.inenergis.model.ElasticContractEntityConverter;
import com.inenergis.util.ElasticActionsUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
@Getter
@Setter
public class ContractEntityService {
    @Inject
    ContractEntityDao contractEntityDao;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    public List<ContractEntity> getByBusinessName(String name) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("businessName").value(name).matchMode(MatchMode.START).build());
        return contractEntityDao.getWithCriteria(conditions);
    }

    public List<ContractEntity> getByDba(String dba) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("dba").value(dba).matchMode(MatchMode.START).build());
        return contractEntityDao.getWithCriteria(conditions);
    }

    @Transactional
    public ContractEntity save(ContractEntity entity) throws IOException {
        final ContractEntity contractEntity = contractEntityDao.saveOrUpdate(entity);
        elasticActionsUtil.indexDocument(contractEntity.getId().toString(), ElasticContractEntityConverter.convert(contractEntity), ENERGY_ARRAY_INDEX, ElasticContractEntity.ELASTIC_TYPE);
        return contractEntity;
    }

    public ContractEntity getById(Long id) {
        return contractEntityDao.getById(id);
    }

    public List<ContractEntity> getAll() {
        return contractEntityDao.getAll();
    }
}
