package com.inenergis.controller.lazyDataModel;


import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.Instruction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.and;

public class LazyAwardDataModel extends LazyIdentifiableEntityDataModel<Award> {
    public LazyAwardDataModel(EntityManager entityManager, Map<String, Object> permanentFilters) {
        super(entityManager, Award.class, permanentFilters);
    }

    @Override
    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        if (key.equals("tradeDateFrom")) {

            criteria.add(
                    and(Restrictions.ge("tradeDate", entry.getValue()))
            );

        } else if (key.equals("tradeDateTo")) {
            criteria.add(
                    and(Restrictions.le("tradeDate", entry.getValue()))
            );

        } else {
            super.addFilter(criteria, key, entry);
        }
    }
}
