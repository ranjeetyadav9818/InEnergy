package com.inenergis.controller.validator;

import com.inenergis.entity.IdentifiableEntity;
import org.omnifaces.util.Messages;
import org.primefaces.component.inputtext.InputText;

import org.apache.commons.beanutils.BeanUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.StringJoiner;

@RequestScoped
@FacesValidator("uniqueEntityValidator")
public class UniqueEntityValidator implements Validator, Serializable {

    @PersistenceContext
    protected EntityManager em;

    /**
     * Generic unique constraint validator for IdentifiableEntity entities<br />
     * requires the following additional attributes on the form element ("<f:attribute>"):<br />
     * - "currentEntity" the entity instance (used for getting the class)<br />
     * - "uniqueColumn" the column where the new value will be checked for uniqueness
     */
    @Override
    public void validate(final FacesContext context, final UIComponent comp, final Object newValue) throws ValidatorException {
        IdentifiableEntity currentEntity = (IdentifiableEntity) comp.getAttributes().get("currentEntity");

        String[] uniqueColumns = ((String) comp.getAttributes().get("uniqueColumns")).split(",");
        String[] fields = ((String) comp.getAttributes().get("fields")).split(",");

        StringJoiner conditions = new StringJoiner(" AND ");

        for (String uniqueColumn : uniqueColumns) {
            conditions.add(uniqueColumn + " = :" + uniqueColumn);
        }

        if (currentEntity.getId() != null) {
            conditions.add("id <> " + currentEntity.getId());
        }

        boolean isValid = false;
        try {
            Query query = em.createQuery("FROM " + currentEntity.getClass().getSimpleName()
                    + " WHERE " + conditions.toString(), currentEntity.getClass());

            for (int i = 0; i < uniqueColumns.length; i++) {
                String uniqueColumn = uniqueColumns[i];
                String attribute;
                if (fields[i].equals("{newValue}")) {
                    attribute = newValue.toString();
                } else {
                    attribute = BeanUtils.getProperty(currentEntity, fields[i]);
                }
                query.setParameter(uniqueColumn, attribute);
            }

            query.getSingleResult();
        } catch (NoResultException ex) {
            isValid = true;
        } catch (Exception e) {
            throw new ValidatorException(Messages.createError("validator wrongly defined"), e);
        }

        if (!isValid) {
            FacesMessage msg = Messages.createError("{0} ({1}) already exists", ((InputText) comp).getLabel(), newValue);
            throw new ValidatorException(msg);
        }
    }
}