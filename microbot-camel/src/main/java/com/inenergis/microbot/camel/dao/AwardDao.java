package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.award.Award;
import com.inenergis.entity.locationRegistration.IsoResource;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface AwardDao extends Repository<Award, Long> {

    Award findFirstByTradeDateGreaterThanEqualAndTradeDateLessThanEqualAndResource(Date fromTradeDate, Date toTradeDate, IsoResource resource);

    Award save(Award award);
}