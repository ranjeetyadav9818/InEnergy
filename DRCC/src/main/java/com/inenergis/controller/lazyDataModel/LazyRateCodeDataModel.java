package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.genericEnum.RateCodeSector;
import com.inenergis.entity.program.rateProgram.RateCode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.and;

public class LazyRateCodeDataModel extends LazyIdentifiableEntityDataModel<RateCode> {

    public LazyRateCodeDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, RateCode.class, preFilters);
    }

    @Override
    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        if (key.equals("sector")) {
            final String filter = entry.getValue().toString();
            final RateCodeSector rateCodeSector = RateCodeSector.valueOf(filter);
            criteria.createAlias("sectors", "se");
            criteria.add(and(Restrictions.in("se.sector", Arrays.asList(rateCodeSector))));
        } else {
            super.addFilter(criteria, key, entry);
        }
    }

}
