package com.inenergis.controller.validator;

import org.primefaces.component.calendar.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.text.SimpleDateFormat;
import java.util.Date;

@FacesValidator("endDateRangeValidator")
public class EndDateRangeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        Date endDate = (Date)value;
        Calendar calendar = (Calendar) component;
        Date startDate ;
        Object startDateValue = component.getAttributes().get("startDate");
        if (startDateValue==null) {
            startDate = (Date) calendar.getMindate();
        }else{
            Calendar startDateCalendar = (Calendar) startDateValue;
            startDate = (Date) startDateCalendar.getValue();
        }

        if (startDate != null && endDate.before(startDate)) {
            SimpleDateFormat sdfDate = new SimpleDateFormat(calendar.getPattern());// create this object takes some time but we only do once a user introduces a wrong date
            String dateFormatted = sdfDate.format(startDate);
            String label = calendar.getLabel();
            FacesMessage message = new FacesMessage(label +" should be after "+dateFormatted);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
