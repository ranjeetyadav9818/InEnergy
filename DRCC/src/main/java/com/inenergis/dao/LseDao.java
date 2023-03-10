package com.inenergis.dao;

import com.inenergis.entity.Lse;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;

@Stateless
@Transactional
public class LseDao extends GenericDao<Lse> {
    public LseDao() {
        setClazz(Lse.class);
    }

    public Lse getByCode(String s) {
        return getUniqueResultWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("code").matchMode(MatchMode.EXACT).value(s).build()));
    }
}
