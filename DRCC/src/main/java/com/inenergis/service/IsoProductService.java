package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.IsoProductDao;
import com.inenergis.entity.marketIntegration.IsoProduct;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Stateless
public class IsoProductService {

    @Inject
    IsoProductDao isoProductDao;

    public void save(IsoProduct isoProduct) {
        isoProductDao.save(isoProduct);
    }

    public List<IsoProduct> getAll() {
        return isoProductDao.getAll();
    }

    public IsoProduct getById(Long id) {
        return isoProductDao.getById(id);
    }
}