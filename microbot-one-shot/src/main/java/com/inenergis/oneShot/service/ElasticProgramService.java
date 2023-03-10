package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.ProgramDao;
import com.inenergis.entity.program.Program;
import com.inenergis.model.ElasticProgramConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticProgram.ELASTIC_TYPE;

/**
 * Created by Antonio on 18/08/2017.
 */
@Component
public class ElasticProgramService extends ElasticService<Program>{

    @Autowired
    private ProgramDao programDao;

    @Transactional
    public boolean sendProgramToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(Program program) {
        return ElasticProgramConverter.convert(program);
    }

    @Override
    protected Page<Program> findAll(int page, int size) {
        return programDao.findAll(new PageRequest(page, size));
    }
}