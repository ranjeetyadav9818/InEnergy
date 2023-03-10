package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.ServicePoint;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface ServicePointDao extends Repository<ServicePoint, Long> {

    ServicePoint getByServicePointId(String id);
}