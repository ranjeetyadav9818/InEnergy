package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.ProgramProductService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter("programProductConverter")
public class ProgramProductConverter extends GenericConverter{

    @Inject
    ProgramProductService programProductService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return programProductService.getById(id);
    }
}
