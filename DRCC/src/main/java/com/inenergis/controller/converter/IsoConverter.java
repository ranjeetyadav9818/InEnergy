package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.AssetService;
import com.inenergis.service.IsoService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "isoConverter")
public class IsoConverter extends GenericConverter {

    @Inject
    private IsoService isoService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return isoService.getIso(id);
    }
}