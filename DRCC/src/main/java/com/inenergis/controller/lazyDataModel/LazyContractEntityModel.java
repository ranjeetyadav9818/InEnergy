package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.contract.ContractEntity;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyContractEntityModel extends LazyIdentifiableEntityDataModel<ContractEntity> {
    public LazyContractEntityModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, ContractEntity.class, preFilters);
    }
}