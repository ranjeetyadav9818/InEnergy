package com.inenergis.service;

import com.inenergis.dao.ServiceAgreementAssetDao;
import com.inenergis.entity.ServiceAgreementAsset;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Getter
public class ServiceAgreementAssetService {

    private static final Logger log = LoggerFactory.getLogger(ServiceAgreementAssetService.class);

    @Inject
    private ServiceAgreementAssetDao serviceAgreementAssetDao;

    public ServiceAgreementAsset getById(Long id) {
        return serviceAgreementAssetDao.getById(id);
    }

    public void save(ServiceAgreementAsset serviceAgreementAsset) {
        log.info("Saving serviceAgreementAsset " + serviceAgreementAsset.toString());
        serviceAgreementAssetDao.saveOrUpdate(serviceAgreementAsset);
    }

    public void delete(ServiceAgreementAsset serviceAgreementAsset) {
        serviceAgreementAssetDao.delete(serviceAgreementAsset);
    }
}
