package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.RegistrationDao;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.model.ElasticRegistration;
import com.inenergis.model.ElasticRegistrationConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.net.UnknownHostException;

@Component
public class ElasticRegistrationService extends ElasticService<RegistrationSubmissionStatus> {

    @Autowired
    private RegistrationDao registrationDao;

    @Transactional
    public boolean sendRegistrationsToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ElasticRegistration.ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(RegistrationSubmissionStatus registration) {
        return ElasticRegistrationConverter.convert(registration);
    }

    @Override
    protected Page<RegistrationSubmissionStatus> findAll(int page, int size) {
        return registrationDao.findAll(new PageRequest(page, size));
    }
}
