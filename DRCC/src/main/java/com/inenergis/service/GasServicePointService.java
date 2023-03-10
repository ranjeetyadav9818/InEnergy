package com.inenergis.service;

import com.inenergis.dao.FeederDao;

import com.inenergis.dao.GasServicePointDao;

import com.inenergis.dao.SubstationDao;
import com.inenergis.entity.Feeder;
import com.inenergis.entity.GasServicePoint;
import com.inenergis.entity.Substation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@Stateless
public class GasServicePointService {

    @Inject
    GasServicePointDao servicePointDao;
    @Inject
    SubstationDao substationDao;
    @Inject
    FeederDao feederDao;

    public List<GasServicePoint> getAll() {
        return servicePointDao.getAll();
    }

    public List<GasServicePoint> getByFeederId(String text) {
        return servicePointDao.getByFeederId(text);
    }

    public List<Substation> getSubstations() {
        return substationDao.getAll();
    }

    public List<Feeder> getFeeders() {
        return feederDao.getAll();
    }

// todogas   public List<String> getDistinctCircuits() {
//        return servicePointDao.getDistinctCircuits();
//    }

    public List<GasServicePoint> getByIds(Collection<String> ids) {
        return servicePointDao.getByIds(ids);
    }

// todogas   public List<GasServicePoint> getBySubstations(List<String> substations) {
//        return servicePointDao.getBySubstations(substations);
//    }

//   todogas  public List<GasServicePoint> getByCircuits(List<String> circuits) {
//        return servicePointDao.getByCircuits(circuits);
//    }
}