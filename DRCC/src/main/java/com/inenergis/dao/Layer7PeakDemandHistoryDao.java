package com.inenergis.dao;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.ServiceAgreement;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Stateless
@Transactional
public class Layer7PeakDemandHistoryDao extends GenericDao<Layer7PeakDemandHistory> {

    public Layer7PeakDemandHistoryDao() {
        setClazz(Layer7PeakDemandHistory.class);
    }

    public List<Layer7PeakDemandHistory> getBy(BaseServiceAgreement sa) {
        return getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("serviceAgreement").matchMode(MatchMode.EXACT).value(sa).build()));
    }
}
