package com.inenergis.service;

import com.inenergis.dao.FeederDao;
import com.inenergis.dao.ServicePointDao;
import com.inenergis.dao.SubstationDao;
import com.inenergis.entity.Feeder;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.Substation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@Stateless
public class ServicePointService {

    @Inject
    ServicePointDao servicePointDao;
    @Inject
    SubstationDao substationDao;
    @Inject
    FeederDao feederDao;

    public List<ServicePoint> getAll() {
        return servicePointDao.getAll();
    }

    public List<ServicePoint> getByFeederId(String text) {
        return servicePointDao.getByFeederId(text);
    }

    public List<Substation> getSubstations() {
        return substationDao.getAll();
    }

    public List<Feeder> getFeeders() {
        return feederDao.getAll();
    }

    public List<String> getDistinctCircuits() {
        return servicePointDao.getDistinctCircuits();
    }

    public List<ServicePoint> getByIds(Collection<String> ids) {
        return servicePointDao.getByIds(ids);
    }

    public List<ServicePoint> getBySubstations(List<String> substations) {
        return servicePointDao.getBySubstations(substations);
    }

    public List<ServicePoint> getByCircuits(List<String> circuits) {
        return servicePointDao.getByCircuits(circuits);
    }
}