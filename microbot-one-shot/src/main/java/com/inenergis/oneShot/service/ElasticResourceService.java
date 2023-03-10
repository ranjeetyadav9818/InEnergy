package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.ResourceDao;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.model.ElasticResourceConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticResource.ELASTIC_TYPE;

@Component
public class ElasticResourceService extends ElasticService<IsoResource> {

    @Autowired
    private ResourceDao resourceDao;

    @Transactional
    public boolean sendResourcesToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(IsoResource resource) {
        return ElasticResourceConverter.convert(resource);
    }

    @Override
    protected Page<IsoResource> findAll(int page, int size) {
        return resourceDao.findAll(new PageRequest(page, size));
    }
}
