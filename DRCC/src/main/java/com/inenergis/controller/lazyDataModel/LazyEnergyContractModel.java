package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.util.DateRange;
import org.hibernate.Criteria;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyEnergyContractModel extends LazyIdentifiableEntityDataModel<EnergyContract> {
    public LazyEnergyContractModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, EnergyContract.class, preFilters);
    }

}