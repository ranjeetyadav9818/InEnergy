package com.inenergis.util;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Antonio on 23/08/2017.
 */
@Stateless
public class ElasticActionsUtil {

    public static final String ENERGY_ARRAY_INDEX = "energy_array";

    @Inject
    ElasticConnectionPool elasticConnectionPool;

    public void indexDocument(String id, Object elasticObject, String index, String type) throws IOException {
        JestClient client = elasticConnectionPool.getClient();
        Index document = new Index.Builder(elasticObject).index(index).type(type).id(id).build();
        client.execute(document);
    }

    public void deleteDocument(String id, String index, String type) throws IOException {
        JestClient client = elasticConnectionPool.getClient();
        Delete delete =  new Delete.Builder(id).index(index).type(type).build();
        client.execute(delete);
    }

}
