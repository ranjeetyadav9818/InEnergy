package com.inenergis.controller.converter;

import com.inenergis.entity.program.ProgramSeason;
import com.inenergis.service.ProgramSeasonService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "programSeasonConverter")
public class ProgramSeasonConverter extends GenericConverter {

    @Inject
    private ProgramSeasonService programSeasonService;

    public ProgramSeason getById(Long id) {
        return programSeasonService.getById(id);
    }
}