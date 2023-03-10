package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Getter
@Setter
public class LazyServiceAgreementApplicationEnrollmentDataModel extends LazyIdentifiableEntityDataModel<ProgramServiceAgreementEnrollment> {

	public LazyServiceAgreementApplicationEnrollmentDataModel(EntityManager entityManager) {
		super(entityManager, ProgramServiceAgreementEnrollment.class);
	}

	public LazyServiceAgreementApplicationEnrollmentDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
		super(entityManager, ProgramServiceAgreementEnrollment.class, preFilters);
	}
}