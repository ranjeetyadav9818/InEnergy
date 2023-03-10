package com.inenergis.controller.validator;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("commaSeparatedNumberValidator")
public class CommaSeparatedNumberValidator implements Validator {

    private static Pattern pattern = Pattern.compile("(\\d*,?)*");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        String numberSeparatedByComma = ((String)value).trim();
        Matcher matcher = pattern.matcher(numberSeparatedByComma);
        if(!matcher.find() || !matcher.hitEnd()){
            String label = "Field";
            if(component instanceof InputText){
                label = ((InputText) component).getLabel();
            } else if(component instanceof InputTextarea){
                label = ((InputTextarea) component).getLabel();
            }
            FacesMessage message = new FacesMessage(label+" should contain only comma separated numbers");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
