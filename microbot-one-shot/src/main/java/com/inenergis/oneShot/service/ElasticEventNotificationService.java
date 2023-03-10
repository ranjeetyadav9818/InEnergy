package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.EventNotificationDao;
import com.inenergis.entity.PdpSrEvent;
import com.inenergis.model.ElasticEventNotification;
import com.inenergis.model.ElasticEventNotificationConverter;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
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

import static com.inenergis.model.ElasticEventNotification.ELASTIC_TYPE;


/**
 * Created by Antonio on 18/08/2017.
 */
@Component
public class ElasticEventNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ElasticEventNotificationService.class);

    @Autowired
    private EventNotificationDao eventNotificationDao;

    @Transactional
    public boolean sendEventNotificationToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {

        try{
            Page<PdpSrEvent> all = eventNotificationDao.findAll(new PageRequest(page, size));
            if(all.getNumberOfElements()>0){
                Bulk.Builder builder = new Bulk.Builder();
                builder.defaultIndex("energy_array").defaultType(ELASTIC_TYPE);
                for (PdpSrEvent eventNotification : all) {
                    ElasticEventNotification elasticEventNotification = ElasticEventNotificationConverter.convert(eventNotification);
                    Index index = new Index.Builder(elasticEventNotification).index("energy_array").type(ELASTIC_TYPE).id(eventNotification.getEventId().toString()).build();
                    builder.addAction(index);
                }
                client.execute(builder.build());
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
