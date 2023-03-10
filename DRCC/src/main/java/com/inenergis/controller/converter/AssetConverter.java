package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.AssetService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "assetConverter")
public class AssetConverter extends GenericConverter {

    @Inject
    private AssetService assetService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return assetService.getById(id);
    }
}