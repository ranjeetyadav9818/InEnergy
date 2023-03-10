package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;

import javax.persistence.EntityManager;
import java.util.Map;


public class LazyRegistrationSubmissionStatusDataModel extends LazyIdentifiableEntityDataModel<RegistrationSubmissionStatus> {
    public LazyRegistrationSubmissionStatusDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, RegistrationSubmissionStatus.class, preFilters);
    }
}
