package com.inenergis.dao;

import com.inenergis.entity.ServiceAgreementAsset;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ServiceAgreementAssetDao extends GenericDao<ServiceAgreementAsset> {

    public ServiceAgreementAssetDao() {
        setClazz(ServiceAgreementAsset.class);
    }
}
