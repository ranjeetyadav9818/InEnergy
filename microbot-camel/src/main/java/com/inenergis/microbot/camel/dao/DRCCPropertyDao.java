package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.DRCCProperty;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface DRCCPropertyDao extends Repository<DRCCProperty, Long> {

    DRCCProperty getByKey(String key);

    DRCCProperty save(DRCCProperty drccProperty);
}