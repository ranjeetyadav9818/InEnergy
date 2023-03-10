package com.inenergis.controller.converter;

import com.inenergis.util.ConstantsProviderModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@FacesConverter(value = "localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s!=null && s.length()<=10){
            return LocalDate.parse(s, ConstantsProviderModel.DATE_FORMATTER).atStartOfDay();
        }
        return LocalDateTime.parse(s, ConstantsProviderModel.DATE_TIME_FORMATTER);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        LocalDateTime localDateTime = (LocalDateTime) o;
        return localDateTime.format(ConstantsProviderModel.DATE_TIME_FORMATTER);
    }
}