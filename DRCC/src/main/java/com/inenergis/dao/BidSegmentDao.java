package com.inenergis.dao;


import com.inenergis.entity.bidding.Segment;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class BidSegmentDao extends GenericDao<Segment> {

    public BidSegmentDao() {
        setClazz(Segment.class);
    }
}
