package com.inenergis.controller.converter;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.entity.program.Program;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "programPickListConverter")
public class ProgramsPickListConverter implements Converter {

    @Inject
    ProgramServiceContract programService;

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        final Program program = programService.getProgram(Long.valueOf(arg2));
        return program;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String str = "";
        if (arg2 instanceof Program) {
            str = ((Program) arg2).getId().toString();
        }
        return str;
    }
}
