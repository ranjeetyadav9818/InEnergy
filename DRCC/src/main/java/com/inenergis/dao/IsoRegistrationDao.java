package com.inenergis.dao;

import com.inenergis.entity.ProductType;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class IsoRegistrationDao extends GenericDao<RegistrationSubmissionStatus>{
    public IsoRegistrationDao(){
        setClazz(RegistrationSubmissionStatus.class);
    }

    public List<RegistrationSubmissionStatus> getByTypeSublapStatusesAndDate(ProductType resourceType, SubLap subLap, List<RegistrationSubmissionStatus.RegistrationStatus> registrationStatuses, Date date, Iso iso) {
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(RegistrationSubmissionStatus.class)
                .createAlias("isoResource", "resource")
                .add(Restrictions.eq("resource.type", resourceType))
                .add(Restrictions.in("registrationStatus", registrationStatuses))
                .add(Restrictions.le("activeStartDate",date))
                .add(Restrictions.gt("activeEndDate",date))
                ;
        if (subLap != null) {
            criteria.add(Restrictions.eq("resource.isoSublap", subLap));
        }
        if (iso != null) {
            criteria.createAlias("resource.isoProduct", "product");
            criteria.createAlias("product.profile", "prodProf");
            criteria.createAlias("prodProf.iso", "iso");
            criteria.add(Restrictions.eq("iso.id", iso.getId()));
        }
        return criteria.list();
    }
}
