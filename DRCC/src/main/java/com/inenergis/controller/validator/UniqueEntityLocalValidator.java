package com.inenergis.controller.validator;

import com.inenergis.entity.IdentifiableEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.omnifaces.util.Messages;
import org.primefaces.component.inputtext.InputText;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@RequestScoped
@FacesValidator("uniqueEntityLocalValidator")
public class UniqueEntityLocalValidator implements Validator, Serializable {

    private Set<String> uniqueRecords = new HashSet<>();

    @Override
    public void validate(final FacesContext context, final UIComponent comp, final Object newValue) throws ValidatorException {
        IdentifiableEntity currentEntity = (IdentifiableEntity) comp.getAttributes().get("currentEntity");
        String[] fields = ((String) comp.getAttributes().get("fields")).split(",");

        StringJoiner recordSignature = new StringJoiner(":");

        try {
            for (String field : fields) {
                String attribute;
                if (field.equals("{newValue}")) {
                    attribute = newValue.toString();
                } else {
                    attribute = BeanUtils.getProperty(currentEntity, field);
                }
                recordSignature.add(attribute);
            }
            if (uniqueRecords.contains(recordSignature.toString())) {
                FacesMessage msg = Messages.createError("{0} ({1}) already exists", ((InputText) comp).getLabel(), newValue);
                throw new ValidatorException(msg);
            }
            uniqueRecords.add(recordSignature.toString());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ValidatorException(Messages.createError("validator wrongly defined"), e);
        }
    }
}