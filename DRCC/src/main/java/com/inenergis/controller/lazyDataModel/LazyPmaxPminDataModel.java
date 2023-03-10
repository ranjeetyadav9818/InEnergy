package com.inenergis.controller.lazyDataModel;


import com.inenergis.entity.locationRegistration.PmaxPmin;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyPmaxPminDataModel extends LazyIdentifiableEntityDataModel<PmaxPmin> {
    public LazyPmaxPminDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, PmaxPmin.class, preFilters);
    }

}