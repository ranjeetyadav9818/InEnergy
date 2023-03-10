package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.Layer7PeakDemandHistory;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyDemandHistoryDataModel extends LazyIdentifiableEntityDataModel<Layer7PeakDemandHistory> {

    public LazyDemandHistoryDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, Layer7PeakDemandHistory.class, preFilters);
    }
}
