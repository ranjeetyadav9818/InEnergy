package com.inenergis.controller.converter;

import com.inenergis.util.ConstantsProviderModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalTime;

@FacesConverter(value = "localTimeConverter")
public class LocalTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        LocalTime time = LocalTime.parse(s, ConstantsProviderModel.HOUR_FORMATTER);
        return time;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof LocalTime) {
            LocalTime localTime = (LocalTime) o;
            if (o == null) {
                return null;
            }
            String sTime = localTime.format(ConstantsProviderModel.HOUR_FORMATTER);
            return sTime;
        } else {
            return null;
        }
    }
}
