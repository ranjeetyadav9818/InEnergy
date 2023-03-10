package com.inenergis.dao;

import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.genericEnum.BidStatus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class BidDao extends GenericDao<Bid> {

    public BidDao() {
        setClazz(Bid.class);
    }

    public Bid getRecentBidResource(Long isoResourceId, Date tradeDate, List<BidStatus> statuses) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());

        criteria.add(Restrictions.eq("isoResource.id", isoResourceId));
        criteria.add(Restrictions.eq("tradeDate", tradeDate));

        if (CollectionUtils.isNotEmpty(statuses)) {
            criteria.add(Restrictions.in("status", statuses));
        }

        return (Bid) criteria.uniqueResult();
    }
}