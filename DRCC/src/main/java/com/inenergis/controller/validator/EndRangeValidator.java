package com.inenergis.controller.validator;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("endRangeValidator")
public class EndRangeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        Integer endRange = (Integer)value;
        SelectOneMenu startRangeMenu = (SelectOneMenu) component.getAttributes().get("startRange");

        if (((Integer) startRangeMenu.getValue()) > endRange) {
            String label = startRangeMenu.getLabel();
            FacesMessage message = new FacesMessage(label +" is wrong. Start of the range cannot be greater than end");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
