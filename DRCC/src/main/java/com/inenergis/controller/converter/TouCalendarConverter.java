package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.TimeOfUseCalendarService;
import com.inenergis.service.TimeOfUseService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "touCalendarConverter")
public class TouCalendarConverter extends GenericConverter {

    @Inject
    TimeOfUseService timeOfUseService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return timeOfUseService.getById(id);
    }
}