package com.inenergis.dao;

import com.inenergis.entity.marketIntegration.IsoProduct;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class IsoProductDao extends GenericDao<IsoProduct> {
    public IsoProductDao() {
        setClazz(IsoProduct.class);
    }
}