package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Antonio on 18/08/2017.
 */
public abstract class ElasticService<T extends IdentifiableEntity> {

    private static final Logger log = LoggerFactory.getLogger(ElasticService.class);
    public static final String ENERGY_ARRAY = "energy_array";

    @Transactional
    public boolean sendObjectToElastic(JestClient client, int page, int size, String type) throws UnknownHostException, JsonProcessingException {
        try{
            Page<T> all = findAll(page, size);
            if(all.getNumberOfElements()>0){
                Bulk.Builder builder = new Bulk.Builder();
                builder.defaultIndex(ENERGY_ARRAY).defaultType(type);
                for (IdentifiableEntity entity : all) {
                    SearchMatch match = getElasticObject((T) entity);
                    Index index = new Index.Builder(match).index(ENERGY_ARRAY).type(type).id(entity.getId().toString()).build();
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

    protected abstract SearchMatch getElasticObject(T entity);

    protected abstract Page<T> findAll(int page, int size);
}
