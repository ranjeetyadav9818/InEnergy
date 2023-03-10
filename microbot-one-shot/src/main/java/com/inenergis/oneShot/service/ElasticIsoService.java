package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.IsoDao;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.model.ElasticIsoConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticISO.ELASTIC_TYPE;

/**
 * Created by Antonio on 18/08/2017.
 */
@Component
public class ElasticIsoService extends ElasticService<Iso> {

    @Autowired
    private IsoDao isoDao;

    @Transactional
    public boolean sendIsoToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(Iso iso) {
        return ElasticIsoConverter.convert(iso);
    }

    @Override
    protected Page<Iso> findAll(int page, int size) {
        return isoDao.findAll(new PageRequest(page, size));
    }
}
