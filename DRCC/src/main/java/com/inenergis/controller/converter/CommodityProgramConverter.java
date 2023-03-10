package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.CommodityProgramService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "commodityProgramConverter")
public class CommodityProgramConverter implements Converter {

    @Inject
    CommodityProgramService commodityProgramService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return commodityProgramService.getById(Long.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = "";
        if (value != null && IdentifiableEntity.class.isAssignableFrom(value.getClass())) {
            str = ((IdentifiableEntity) value).getId().toString();
        }
        return str;
    }
}