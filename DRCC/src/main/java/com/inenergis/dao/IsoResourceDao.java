package com.inenergis.dao;

import com.inenergis.entity.locationRegistration.IsoResource;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Stateless
@Transactional
public class IsoResourceDao extends GenericDao<IsoResource> {
    public IsoResourceDao() {
        setClazz(IsoResource.class);
    }

    public List<IsoResource> findWithNameLike(String text, Integer limit) {
        return getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("name").matchMode(MatchMode.START).value(text).build()), limit);
    }
}
