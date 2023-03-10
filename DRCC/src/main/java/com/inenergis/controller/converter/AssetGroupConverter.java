package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.AssetGroupService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "assetGroupConverter")
public class AssetGroupConverter extends GenericConverter {

    @Inject
    private AssetGroupService assetGroupService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return assetGroupService.getById(id);
    }
}