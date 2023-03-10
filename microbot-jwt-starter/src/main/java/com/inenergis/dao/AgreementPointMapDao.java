package com.inenergis.dao;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 22/09/2017.
 */
@Component
public interface AgreementPointMapDao extends Repository<AgreementPointMap,Long>{

    @Transactional("mysqlTransactionManager")
    List<AgreementPointMap> getAllByServiceAgreementIn(List<ServiceAgreement> ids);
}
