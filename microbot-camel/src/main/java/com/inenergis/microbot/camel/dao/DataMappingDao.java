package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface DataMappingDao extends Repository<DataMapping, Long> {

    DataMapping getFirstByTypeAndSource(DataMappingType type, String config);
}