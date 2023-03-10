package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class GenericConverter<T extends IdentifiableEntity> implements Converter {

    public abstract T getById(Long id);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return getById(Long.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            final Long id = ((IdentifiableEntity) value).getId();
            if (id != null) {
                return id.toString();
            }
        }
        return StringUtils.EMPTY;
    }
}