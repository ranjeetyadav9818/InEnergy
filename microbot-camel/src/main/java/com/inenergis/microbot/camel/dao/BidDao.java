package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.locationRegistration.IsoResource;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public interface BidDao extends Repository<Bid, Long> {

    Bid findFirstByTradeDateEqualsAndIsoResource(Date date, IsoResource resource);

    List<Bid> findAllByTradeDateBetweenAndIsoResourceIn(Date startDate, Date endDate, Collection<IsoResource> resources);

    List<Bid> findAllByTradeDateBetweenAndStatusIn(Date startDate, Date endDate, Collection<BidStatus> statuses);

    List<Bid> findAllByTradeDateGreaterThanEqualAndStatusInAndDefaultSchedule(Date startDate, Collection<BidStatus> statuses, boolean defaultSchedule);

    List<Bid> findAllByStatus(BidStatus status);

    Bid save(Bid bid);
}