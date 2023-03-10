package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.ContractDao;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.model.ElasticContractConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticContract.ELASTIC_TYPE;

/**
 * Created by egamas on 04/09/2017.
 */
@Component
public class ElasticContractService extends ElasticService<EnergyContract> {

    @Autowired
    private ContractDao contractDao;  
    
    @Transactional
    public boolean sendContractToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(EnergyContract contract) {
        return ElasticContractConverter.convert(contract);
    }

    @Override
    protected Page<EnergyContract> findAll(int page, int size) {
        return contractDao.findAll(new PageRequest(page, size));
    }
}
