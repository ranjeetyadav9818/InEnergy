package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.inenergis.oneShot.dao.AggregatorDao;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.model.AggregatorConverter;
import com.inenergis.model.ElasticAggregator;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

/**
 * Created by Antonio on 18/08/2017.
 */
@Component
public class ElasticAggregatorService extends ElasticService<ProgramAggregator> {

    @Autowired
    private AggregatorDao aggregatorDao;

    @Transactional
    public boolean sendAggregatorsToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client,page,size, ElasticAggregator.ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(ProgramAggregator aggregator) {
        return AggregatorConverter.convert(aggregator);
    }

    @Override
    protected Page<ProgramAggregator> findAll(int page, int size) {
        return aggregatorDao.findAll(new PageRequest(page, size));
    }
}
