package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.EntityDao;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.model.ElasticContractEntity;
import com.inenergis.model.ElasticContractEntityConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

/**
 * Created by egamas on 01/09/2017.
 */
@Component
public class ElasticEntityService extends ElasticService<ContractEntity>{

    @Autowired
    private EntityDao entityDao;

    @Transactional
    public boolean sendContractEntityToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ElasticContractEntity.ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(ContractEntity contractEntity) {
        return ElasticContractEntityConverter.convert(contractEntity);
    }

    @Override
    protected Page<ContractEntity> findAll(int page, int size) {
        return entityDao.findAll(new PageRequest(page, size));
    }
}