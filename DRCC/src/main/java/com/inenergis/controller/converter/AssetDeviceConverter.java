package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.AssetDeviceService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "assetDeviceConverter")
public class AssetDeviceConverter extends GenericConverter {

    @Inject
    private AssetDeviceService assetDeviceService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return assetDeviceService.getById(id);
    }
}