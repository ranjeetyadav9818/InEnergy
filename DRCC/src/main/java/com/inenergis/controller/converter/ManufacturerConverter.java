package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.ManufacturerService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "manufacturerConverter")
public class ManufacturerConverter extends GenericConverter {

    @Inject
    ManufacturerService manufacturerService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return manufacturerService.getById(id);
    }
}