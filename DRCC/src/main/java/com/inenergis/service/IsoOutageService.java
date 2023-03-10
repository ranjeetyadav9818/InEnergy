package com.inenergis.service;

import com.inenergis.dao.IsoOutageDao;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.program.ImpactedResource;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Stateless
public class IsoOutageService {

    @Inject
    IsoOutageDao isoOutageDao;

    public List<IsoOutage> getByResources(Collection<IsoResource> resources, Date date) {
        if (CollectionUtils.isEmpty(resources)) {
            return new ArrayList<>();
        }
        return isoOutageDao.getByResources(resources, date);
    }

    public IsoOutage getByOrDefault(IsoResource resource, Date date, IsoOutage defaultIsoOutage) {
        IsoOutage isoOutage = isoOutageDao.getByResource(resource, date);
        if (isoOutage == null) {
            isoOutage = defaultIsoOutage;
        }

        return isoOutage;
    }

    public IsoOutage getByImpactedResource(ImpactedResource impactedResource) {
        return isoOutageDao.getByResource(impactedResource.getIsoResource(), impactedResource.getEvent().getStartDate());
    }

    public IsoOutage getByImpactedResourceOrDefault(ImpactedResource impactedResource, IsoOutage defaultIsoOutage) {
        IsoOutage isoOutage = getByImpactedResource(impactedResource);

        if (isoOutage != null) {
            return isoOutage;
        }

        return defaultIsoOutage;
    }

    public void saveOrUpdate(IsoOutage isoOutage) {
        isoOutageDao.saveOrUpdate(isoOutage);
    }

    public void delete(IsoOutage isoOutage) {
        isoOutageDao.delete(isoOutage);
    }
}