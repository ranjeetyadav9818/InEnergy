package com.inenergis.dao;

import com.inenergis.entity.PricingNode;
import com.inenergis.entity.SubLap;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;

@Stateless
@Transactional
public class PricingNodeDao extends GenericDao<PricingNode> {
    public PricingNodeDao() {
        setClazz(PricingNode.class);
    }

}
