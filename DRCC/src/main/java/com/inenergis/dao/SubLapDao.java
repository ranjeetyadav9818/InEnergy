package com.inenergis.dao;

import com.inenergis.entity.SubLap;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;

@Stateless
@Transactional
public class SubLapDao extends GenericDao<SubLap> {
    public SubLapDao() {
        setClazz(SubLap.class);
    }

    public SubLap getByCode(String s) {
        return getUniqueResultWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("code").matchMode(MatchMode.EXACT).value(s).build()));
    }
}
