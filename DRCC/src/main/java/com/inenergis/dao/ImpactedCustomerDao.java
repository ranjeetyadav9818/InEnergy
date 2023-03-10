package com.inenergis.dao;

import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.ImpactedCustomer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class ImpactedCustomerDao extends GenericDao<ImpactedCustomer> {
    public ImpactedCustomerDao() {
        setClazz(ImpactedCustomer.class);
    }

    @SuppressWarnings("unchecked")
    public List<ImpactedCustomer> getByLocations(List<LocationSubmissionStatus> locations, Date fromDate, List<EventStatus> statuses, List<DispatchReason> dispatchReasons) {
        if (locations.isEmpty()) {
            return new ArrayList<>();
        }

        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ImpactedCustomer.class)
                .createAlias("event", "e")
                .add(Restrictions.in("locationSubmissionStatus", locations))
                .add(Restrictions.ge("e.startDate", fromDate));

        if (!statuses.isEmpty()) {
            criteria.add(Restrictions.in("e.status", statuses));
        }

        if (!dispatchReasons.isEmpty()) {
            criteria.add(Restrictions.in("e.dispatchReason", dispatchReasons));
        }

        return criteria.list();
    }
}