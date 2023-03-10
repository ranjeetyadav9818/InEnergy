package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.RateSeasonService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "rateSeasonConverter")
public class RateSeasonConverter extends GenericConverter {

    @Inject
    RateSeasonService rateSeasonService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return rateSeasonService.getById(id);
    }
}