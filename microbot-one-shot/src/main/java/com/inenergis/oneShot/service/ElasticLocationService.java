package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.LocationDao;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.model.ElasticLocationConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticLocation.ELASTIC_TYPE;

@Component
public class ElasticLocationService extends ElasticService<LocationSubmissionStatus> {

    @Autowired
    private LocationDao locationDao;

    @Transactional
    public boolean sendLocationsToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(LocationSubmissionStatus location) {
        return ElasticLocationConverter.convert(location);
    }

    @Override
    protected Page<LocationSubmissionStatus> findAll(int page, int size) {
        return locationDao.findAll(new PageRequest(page, size));
    }
}
