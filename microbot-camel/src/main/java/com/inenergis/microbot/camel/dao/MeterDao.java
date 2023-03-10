package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.Meter;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface MeterDao extends Repository<Meter, Long> {

    Meter getByMeterId(String id);
}