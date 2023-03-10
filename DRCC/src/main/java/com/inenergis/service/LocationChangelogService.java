package com.inenergis.service;

import com.inenergis.dao.LocationChangelogDao;
import com.inenergis.entity.locationRegistration.LocationChangelog;

import com.inenergis.entity.marketIntegration.Iso;
import org.picketlink.Identity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class LocationChangelogService {

    @Inject
    LocationChangelogDao locationChangelogDao;

    @Inject
    Identity identity;

    public void save(LocationChangelog locationChangelog) {
        locationChangelog.setUserId((identity.getAccount()).getId());
        locationChangelogDao.saveOrUpdate(locationChangelog);
    }

    public List<LocationChangelog> getAll() {
        return locationChangelogDao.getAll();
    }

    public List<LocationChangelog> geAllUnProcessed(Iso iso) {
        return locationChangelogDao.getAllUnProcessed(iso);
    }

    public LocationChangelog getById(Long id) {
        return locationChangelogDao.getById(id);
    }

    public void delete(LocationChangelog locationChangelog) {
        locationChangelogDao.delete(locationChangelog);
    }
}