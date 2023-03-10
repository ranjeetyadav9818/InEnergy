package com.inenergis.service;

import com.inenergis.dao.ServiceAgreementDao;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Stateless
public class ServiceAgreementService {

    private static final Logger log = LoggerFactory.getLogger(ServiceAgreementService.class);

    @Inject
    ServiceAgreementDao serviceAgreementDao;

    public List<ServiceAgreement> getListById(String... ids) {
        return serviceAgreementDao.getAllByIds(Arrays.asList(ids));
    }

    public List<ServiceAgreement> getAllById(List<String> ids) {
        return serviceAgreementDao.getAllByIds(ids);
    }

    public BaseServiceAgreement getById(String id) {
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