package com.inenergis.controller.converter;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.service.TimeOfUseCalendarService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "timeOfUseCalendarConverter")
public class TimeOfUseCalendarConverter extends GenericConverter {

    @Inject
    private TimeOfUseCalendarService timeOfUseCalendarService;

    @Override
    public IdentifiableEntity getById(Long id) {
        return timeOfUseCalendarService.getById(id);
    }
}