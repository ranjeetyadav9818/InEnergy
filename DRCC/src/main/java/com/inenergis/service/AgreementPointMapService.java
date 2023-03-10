package com.inenergis.service;

import com.inenergis.commonServices.AgreementPointMapServiceContract;
import com.inenergis.dao.AgreementPointMapDao;
import com.inenergis.entity.AgreementPointMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AgreementPointMapService implements AgreementPointMapServiceContract {

    private static final Logger log = LoggerFactory.getLogger(AgreementPointMapService.class);

    @Inject
    AgreementPointMapDao agreementPointMapDao;

    public List<AgreementPointMap> getListByIds(List<String> ids) {
        return agreementPointMapDao.getAllByIds(ids);
    }
}