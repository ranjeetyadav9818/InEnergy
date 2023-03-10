package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.billing.BillingException;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyBillingExceptionDataModel extends LazyIdentifiableEntityDataModel<BillingException> {
    public LazyBillingExceptionDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, BillingException.class, preFilters);
    }
}