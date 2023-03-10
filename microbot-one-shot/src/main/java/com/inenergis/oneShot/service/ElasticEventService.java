package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.EventDao;
import com.inenergis.entity.Event;
import com.inenergis.model.ElasticEventConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticEvent.ELASTIC_TYPE;

/**
 * Created by Antonio on 18/08/2017.
 */
@Component
public class ElasticEventService extends ElasticService<Event> {

    @Autowired
    private EventDao eventDao;

    @Transactional
    public boolean sendEventToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(Event event) {
        return ElasticEventConverter.convert(event);
    }

    @Override
    protected Page<Event> findAll(int page, int size) {
        return eventDao.findAll(new PageRequest(page, size));
    }
}
