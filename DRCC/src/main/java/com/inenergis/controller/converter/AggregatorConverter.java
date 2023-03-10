package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.ProgramAggregatorService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "aggregatorConverter")
public class AggregatorConverter implements Converter {

    @Inject
    ProgramAggregatorService programAggregatorService;

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        try {
            return programAggregatorService.getAggregatorById(Long.valueOf(arg2));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String str = "";
        if (IdentifiableEntity.class.isAssignableFrom(arg2.getClass())) {
            str = ((IdentifiableEntity) arg2).getId().toString();
        }
        return str;
    }
}
