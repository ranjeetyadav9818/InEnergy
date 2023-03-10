package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.IsoOutage;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface IsoOutageDao extends Repository<IsoOutage, Long> {

    IsoOutage findFirstByIsoResourceIdAndDateGreaterThanEqualAndDateLessThanEqual(Long id, Date dateOne, Date dateTwo);
}