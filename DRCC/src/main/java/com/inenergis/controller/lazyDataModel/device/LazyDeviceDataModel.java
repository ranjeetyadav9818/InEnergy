package com.inenergis.controller.lazyDataModel.device;

import com.inenergis.controller.lazyDataModel.LazyIdentifiableEntityDataModel;
import com.inenergis.entity.device.AssetDevice;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyDeviceDataModel extends LazyIdentifiableEntityDataModel<AssetDevice> {

    public LazyDeviceDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, AssetDevice.class, preFilters);
    }
}