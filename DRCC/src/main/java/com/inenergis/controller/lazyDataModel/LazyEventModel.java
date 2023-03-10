package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.Event;
import com.inenergis.entity.genericEnum.EventType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.and;

public class LazyEventModel extends LazyIdentifiableEntityDataModel<Event> {
    public LazyEventModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, Event.class, preFilters);
    }

    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        if (key.equals("TYPE")) {
            switch ((EventType) entry.getValue()) {
                case COMPLETED:
                    criteria.add(Restrictions.lt("endDate", new Date()));
                    break;
                case SCHEDULED:
                    criteria.add(Restrictions.gt("startDate", new Date()));
                    break;
                case IN_PROGRESS:
                    criteria.add(
                            and(
                                    Restrictions.le("startDate", new Date()),
                                    Restrictions.ge("endDate", new Date())
                            )
                    );
            }
        } else {
            super.addFilter(criteria, key, entry);
        }
    }
}