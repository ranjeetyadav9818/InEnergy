package com.inenergis.controller.converter;

import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.service.ContractEntityService;
import org.apache.commons.collections.CollectionUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.List;

@FacesConverter(value = "contractEntityConverter")
public class ContractEntityConverter implements Converter {

    @Inject
    ContractEntityService service;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        final String[] split = value.split("\\(");
        String query =null;
        if (split.length > 0) {
            query = split[0].trim();
        }
        final List<ContractEntity> byBusinessName = service.getByBusinessName(query);
        if (CollectionUtils.isNotEmpty(byBusinessName)) {
            return byBusinessName.get(0);
        } else {
            final List<ContractEntity> byDba = service.getByDba(query);
            if (CollectionUtils.isNotEmpty(byDba)) {
                return byDba.get(0);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String str = "";
        if (value != null && ContractEntity.class.isAssignableFrom(value.getClass())) {
            ContractEntity entity = (ContractEntity) value;
            str = entity.getBusinessName()+" ( "+entity.getDba()+" )";
        }
        return str;
    }
}
