package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.IsoResourceDao;
import com.inenergis.dao.PmaxPminDao;
import com.inenergis.entity.Lse;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.PmaxPmin;
import com.inenergis.entity.marketIntegration.Iso;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
public class IsoResourceService {

    @Inject
    IsoResourceDao isoResourceDao;

    @Inject
    PmaxPminDao pmaxPminDao;

    public void saveIso(IsoResource isoResource) {

        isoResourceDao.saveOrUpdate(isoResource);
    }

    public List<IsoResource> getAll() {
        return isoResourceDao.getAll();
    }

    public IsoResource getById(Long id) {
        return isoResourceDao.getById(id);
    }

    public List<IsoResource> findWithNameLike(String text) {
        return findWithNameLike(text, null);
    }

    public List<IsoResource> findWithNameLike(String text, Integer limit) {
        return isoResourceDao.findWithNameLike(text, limit);
    }

    public void savePmaxPmin(PmaxPmin transientPmaxPmin) {
        pmaxPminDao.saveOrUpdate(transientPmaxPmin);
    }

    public List<IsoResource> searchBy(SubLap subLap, Lse lse, IsoResource isoResource, Iso iso) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        if (subLap != null) {
            conditions.add(CriteriaCondition.builder().key("isoSublap").value(subLap.getCode()).matchMode(MatchMode.EXACT).build());
        }

        if (lse != null) {
            conditions.add(CriteriaCondition.builder().key("isoLse").value(lse.getCode()).matchMode(MatchMode.EXACT).build());
        }

        if (isoResource != null) {
            conditions.add(CriteriaCondition.builder().key("name").value(isoResource.getName()).matchMode(MatchMode.EXACT).build());
        }
        if (iso != null) {
            conditions.add(CriteriaCondition.builder().key("isoProduct.profile.iso.id").value(iso.getId()).matchMode(MatchMode.EXACT).build());
        }
        return isoResourceDao.getWithCriteria(conditions);
    }

    public List<IsoResource> getByIds(Collection<Long> ids) {
        return isoResourceDao.getByIds(ids);
    }

    public List<IsoResource> getWithCriteria(List<CriteriaCondition> conditions) {
        return isoResourceDao.getWithCriteria(conditions);
    }

    public void saveResource(IsoResource resource) {
        isoResourceDao.save(resource);
    }
}