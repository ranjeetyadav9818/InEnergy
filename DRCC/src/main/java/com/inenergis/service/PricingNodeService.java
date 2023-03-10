package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.PricingNodeDao;
import com.inenergis.dao.PricingNodeDao;
import com.inenergis.entity.PricingNode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PricingNodeService {

    @Inject
    PricingNodeDao pricingNodeDao;

    public void saveOrUpdate(PricingNode subLap) {
        pricingNodeDao.saveOrUpdate(subLap);
    }

    public List<PricingNode> getAll() {
        return pricingNodeDao.getAll();
    }

    public List<PricingNode> get(int limit) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        return pricingNodeDao.getWithCriteria(conditions, limit);
    }

    public PricingNode getById(Long id) {
        return pricingNodeDao.getById(id);
    }
}