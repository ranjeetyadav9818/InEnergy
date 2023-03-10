package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.contract.ContractDevice;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyContractDeviceDataModel extends LazyIdentifiableEntityDataModel<ContractDevice>{
    public LazyContractDeviceDataModel(EntityManager entityManager, Map<String, Object> permanentFilters) {
        super(entityManager, ContractDevice.class, permanentFilters);
    }
}
