package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.inenergis.oneShot.dao.AgreementPointMapDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.model.APMConverter;
import com.inenergis.model.ElasticAgreementPointMap;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Antonio on 18/08/2017.
 */
@Component
public class AgreementPointMapService {

    private static final Logger log = LoggerFactory.getLogger(AgreementPointMapService.class);

    @Autowired
    private AgreementPointMapDao agreementPointMapDao;

    // instance a json mapper
    ObjectMapper mapper = new ObjectMapper(); // create once, reuse


    @Transactional
    public boolean sendAPMToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {

        try{
            Page<AgreementPointMap> all = agreementPointMapDao.findAll(new PageRequest(page, size));
            if(all.getNumberOfElements()>0){
                Bulk.Builder builder = new Bulk.Builder();
                builder.defaultIndex("energy_array").defaultType("apm");
                for (AgreementPointMap agreementPointMap : all) {
                    ElasticAgreementPointMap elasticAPM = APMConverter.convert(agreementPointMap);
                    Index index = new Index.Builder(elasticAPM).index("energy_array").type("apm").id(elasticAPM.getApmId()).build();
                    builder.addAction(index);
                }
                BulkResult execute = client.execute(builder.build());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            log.error("IOException in the scheduled task", e);
            return false;
        } finally {
            client.shutdownClient();
        }
    }
}
