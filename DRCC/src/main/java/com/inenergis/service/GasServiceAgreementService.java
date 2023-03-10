package com.inenergis.service;

import com.inenergis.dao.GasServiceAgreementDao;
import com.inenergis.entity.GasServiceAgreement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Stateless
public class GasServiceAgreementService {

    private static final Logger log = LoggerFactory.getLogger(GasServiceAgreementService.class);

    @Inject
    GasServiceAgreementDao serviceAgreementDao;

    public List<GasServiceAgreement> getListById(String... ids) {
        return serviceAgreementDao.getAllByIds(Arrays.asList(ids));
    }

    public List<GasServiceAgreement> getAllById(List<String> ids) {
        return serviceAgreementDao.getAllByIds(ids);
    }

    public GasServiceAgreement getById(String id) {
        return serviceAgreementDao.getById(id);
    }

    public List<String> getDistinctHas3rdPartyValues() {
        return serviceAgreementDao.getDistinctHas3rdPartyValues();
    }

    public List<String> getDistinctCustClassCdValues() {
        return serviceAgreementDao.getDistinctCustClassCdValues();
    }

    public List<String> getDistinctCustSizeValues() {
        return serviceAgreementDao.getDistinctCustSizeValues();
    }

}