package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionException;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyRegistrationSubmissionExceptionDataModel extends LazyIdentifiableEntityDataModel<RegistrationSubmissionException> {

    public LazyRegistrationSubmissionExceptionDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, RegistrationSubmissionException.class, preFilters);
    }
}