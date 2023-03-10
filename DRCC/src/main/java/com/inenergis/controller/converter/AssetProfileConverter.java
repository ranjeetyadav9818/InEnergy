package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.AssetProfileService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "assetProfileConverter")
public class AssetProfileConverter extends GenericConverter {

    @Inject
    private AssetProfileService assetProfileService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return assetProfileService.getById(id);
    }
}