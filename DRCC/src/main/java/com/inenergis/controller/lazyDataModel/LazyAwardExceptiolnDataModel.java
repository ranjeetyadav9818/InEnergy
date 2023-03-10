package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.award.AwardException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.and;


public class LazyAwardExceptiolnDataModel extends LazyIdentifiableEntityDataModel<AwardException> {
    public LazyAwardExceptiolnDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, AwardException.class, preFilters);
    }
    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        if (key.equals("tradeDateFrom")) {

            criteria.add(
                    and(Restrictions.ge("dateAdded", entry.getValue()))
            );

        } else if (key.equals("tradeDateTo")) {
            criteria.add(
                    and(Restrictions.le("dateAdded", entry.getValue()))
            );

        } else {
            super.addFilter(criteria, key, entry);
        }
    }
}