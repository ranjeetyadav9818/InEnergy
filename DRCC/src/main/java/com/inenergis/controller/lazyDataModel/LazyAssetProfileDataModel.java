package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.assetTopology.AssetProfile;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyAssetProfileDataModel extends LazyIdentifiableEntityDataModel<AssetProfile>{
    public LazyAssetProfileDataModel(EntityManager entityManager, Map<String, Object> permanentFilters) {
        super(entityManager, AssetProfile.class, permanentFilters);
    }
}
