package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.marketIntegration.Iso;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyIsoModel extends LazyIdentifiableEntityDataModel<Iso> {
    public LazyIsoModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, Iso.class, preFilters);
    }
}