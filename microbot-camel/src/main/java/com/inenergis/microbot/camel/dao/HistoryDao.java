package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.History;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface HistoryDao extends Repository<History, Long> {

    void save(History history);
}