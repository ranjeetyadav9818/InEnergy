package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.CurrencyConfigService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "currencyConfigConverter")
public class CurrencyConfigConverter extends GenericConverter {

    @Inject
    private CurrencyConfigService currencyConfigService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return currencyConfigService.getById(id);
    }
}