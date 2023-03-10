package com.inenergis.dao;

import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egamas on 04/10/2017.
 */
@Component
public interface ProgramServiceAgreementEnrollmentAttributeDao extends Repository<ProgramServiceAgreementEnrollmentAttribute,Long> {

    @Transactional("mysqlTransactionManager")
    @Modifying
    void save(ProgramServiceAgreementEnrollmentAttribute attribute);
}
