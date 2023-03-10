package com.inenergis.service;

import com.inenergis.commonServices.GasAgreementPointMapServiceContract;

import com.inenergis.dao.GasAgreementPointMapDao;
import com.inenergis.entity.AgreementPointMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class GasAgreementPointMapService implements GasAgreementPointMapServiceContract {

    private static final Logger log = LoggerFactory.getLogger(GasAgreementPointMapService.class);

    @Inject
    GasAgreementPointMapDao agreementPointMapDao;

    public List<AgreementPointMap> getListByIds(List<String> ids) {
        return agreementPointMapDao.getAllByIds(ids);
    }
}