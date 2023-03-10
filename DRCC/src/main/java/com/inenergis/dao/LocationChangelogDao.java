package com.inenergis.dao;

import com.inenergis.entity.locationRegistration.LocationChangelog;
import com.inenergis.entity.marketIntegration.Iso;
import org.hibernate.criterion.MatchMode;
import org.picketlink.Identity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Stateless
@Transactional
public class LocationChangelogDao extends GenericDao<LocationChangelog> {

    public LocationChangelogDao() {
        setClazz(LocationChangelog.class);
    }

    public List<LocationChangelog> getAllUnProcessed(Iso iso) {

        return getWithCriteria(Arrays.asList(
                CriteriaCondition.builder().key("processed").matchMode(MatchMode.EXACT).value(false).build(),
                CriteriaCondition.builder().key("isoResource.isoProduct.profile.iso.id").matchMode(MatchMode.EXACT).value(iso.getId()).build()
        ));
    }
}
