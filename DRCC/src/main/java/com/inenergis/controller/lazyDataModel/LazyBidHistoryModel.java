package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.bidding.Bid;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyBidHistoryModel extends LazyIdentifiableEntityDataModel<Bid> {
    public LazyBidHistoryModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, Bid.class, preFilters);
    }
}