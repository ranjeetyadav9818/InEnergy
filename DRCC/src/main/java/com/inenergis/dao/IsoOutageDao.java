package com.inenergis.dao;

import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.util.TimeUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Transactional
public class IsoOutageDao extends GenericDao<IsoOutage> {
    public IsoOutageDao() {
        setClazz(IsoOutage.class);
    }

    @SuppressWarnings("unchecked")
    public List<IsoOutage> getByResources(Collection<IsoResource> resources, Date date) {
        List<Long> ids = resources.stream()
                .map(IsoResource::getId)
                .collect(Collectors.toList());

        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(IsoOutage.class)
                .add(Restrictions.in("isoResource.id", ids))
                .add(Restrictions.eq("date", date));

        return criteria.list();
    }

    public IsoOutage getByResource(IsoResource resource, Date date) {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(IsoOutage.class)
                .add(Restrictions.eq("isoResource.id", resource.getId()))
                .add(Restrictions.ge("date", TimeUtil.getStartOfDay(date)))
                .add(Restrictions.le("date", TimeUtil.getEndOfDay(date)));

        return (IsoOutage) criteria.uniqueResult();
    }
}
