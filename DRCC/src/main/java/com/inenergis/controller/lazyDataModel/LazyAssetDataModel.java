package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.asset.Asset;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyAssetDataModel extends LazyIdentifiableEntityDataModel<Asset> {

    public LazyAssetDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, Asset.class, preFilters);
    }
}