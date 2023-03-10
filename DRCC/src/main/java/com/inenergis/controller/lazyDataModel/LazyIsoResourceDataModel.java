package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.locationRegistration.IsoResource;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyIsoResourceDataModel extends LazyIdentifiableEntityDataModel<IsoResource> {

    public LazyIsoResourceDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, IsoResource.class, preFilters);
    }
}
