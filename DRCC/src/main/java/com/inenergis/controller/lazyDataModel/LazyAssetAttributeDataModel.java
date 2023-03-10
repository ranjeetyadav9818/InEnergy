package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.assetTopology.AssetAttribute;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyAssetAttributeDataModel extends LazyIdentifiableEntityDataModel<AssetAttribute> {
    public LazyAssetAttributeDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, AssetAttribute.class, preFilters);
    }
}