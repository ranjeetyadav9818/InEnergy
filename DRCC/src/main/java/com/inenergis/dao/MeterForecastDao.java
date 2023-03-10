package com.inenergis.dao;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.trove.MeterForecast;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Transactional
public class MeterForecastDao extends GenericDao<MeterForecast> {

    public MeterForecastDao() {
        setClazz(MeterForecast.class);
    }

    @SuppressWarnings("unchecked")
    public List<MeterForecast> getBy(List<String> saIds, String measureType, Date tradeDate) {
        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(MeterForecast.class);

        criteria.add(Restrictions.in("serviceAgreement.id", saIds));
        criteria.add(Restrictions.eq("measureType", measureType));
        criteria.add(Restrictions.eq("measureDate", tradeDate));

        return criteria.list();
    }

    public List<MeterForecast> getBy(String measureType, List<BaseServiceAgreement> serviceAgreements, Date start, Date end) {

        final List<String> ids = serviceAgreements.stream().map(s -> s.getServiceAgreementId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }

        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(MeterForecast.class);
        criteria.add(Restrictions.eq("measureType", measureType));
        criteria.add(Restrictions.in("serviceAgreement.id", ids));
        criteria.add(Restrictions.ge("measureDate", start));
        criteria.add(Restrictions.lt("measureDate", end));
        return criteria.list();

    }
}
