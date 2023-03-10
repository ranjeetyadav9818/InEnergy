package com.inenergis.service;

import com.inenergis.dao.PremiseDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PremiseService {

    @Inject
    PremiseDao premiseDao;

    public List<String> getDistinctPremiseTypeValues() {
        return premiseDao.getDistinctPremiseTypeValues();
    }
}