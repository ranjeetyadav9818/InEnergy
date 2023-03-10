package com.inenergis.service;

import com.inenergis.commonServices.AgreementPointMapServiceContract;
import com.inenergis.dao.AgreementPointMapDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 13/10/2017.
 */
@Component
public class AgreementPointMapService implements AgreementPointMapServiceContract {

    @Autowired
    public AgreementPointMapDao agreementPointMapDao;

    @Override
    @Transactional("mysqlTransactionManager")
    public List<AgreementPointMap> getListByIds(List<String> ids) {
        final List<ServiceAgreement> sas = ids.stream().map(id -> {
            final ServiceAgreement serviceAgreement = new ServiceAgreement();
            serviceAgreement.setServiceAgreementId(id);
            return serviceAgreement;
        }).collect(Collectors.toList());
        return agreementPointMapDao.getAllByServiceAgreementIn(sas);
    }
}
