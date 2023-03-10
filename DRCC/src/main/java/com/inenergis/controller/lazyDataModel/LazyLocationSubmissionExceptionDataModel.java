package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import org.hibernate.Criteria;

import javax.persistence.EntityManager;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.or;

public class LazyLocationSubmissionExceptionDataModel extends LazyIdentifiableEntityDataModel<LocationSubmissionException> {

    public LazyLocationSubmissionExceptionDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, LocationSubmissionException.class, preFilters);
    }

    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        if(key.endsWith(".customerName")){
            criteria.add(
                    or(
                            like(key, getFilterValue(entry).toString(), getMatchMode(entry.getKey())),
                            like(key.replace(".customerName", ".businessName"), getFilterValue(entry).toString(), getMatchMode(entry.getKey()))
                    )
            );
        }else{
            super.addFilter(criteria,key,entry);
        }
    }

}
