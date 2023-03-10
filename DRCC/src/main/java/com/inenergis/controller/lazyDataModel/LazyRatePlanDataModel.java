package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.program.RatePlan;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyRatePlanDataModel extends LazyIdentifiableEntityDataModel<RatePlan> {

    public LazyRatePlanDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, RatePlan.class, preFilters);
    }
}
