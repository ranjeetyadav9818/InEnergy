package com.inenergis.service;

import com.inenergis.dao.AttributeDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.assetTopology.AssetProfileAttribute;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AttributeService {

    @Inject
    AttributeDao attributeDao;

    public Long countByProfileAttribute(AssetProfileAttribute attributeTemplate) {
        List<CriteriaCondition> criteriaConditions = new ArrayList<>();
        criteriaConditions.add(CriteriaCondition.builder().key("profileAttribute").value(attributeTemplate).matchMode(MatchMode.EXACT).build());
        return attributeDao.countWithCriteria(criteriaConditions);
    }

}
