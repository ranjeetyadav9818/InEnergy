package com.inenergis.billingEngine.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by egamas on 08/09/2017.
 */
@Component
public class ElasticClientService {

    public static final String ENERGY_ARRAY_INDEX = "energy_array";
    public static final String INVOICE_MODIFICATIONS_POPULATION_TO_ELASTIC_SEARCH_FAILED = "Invoice modifications population to Elastic Search failed";

    @Value("${elastic.url}")
    private String elasticUrl;
    @Value("${elastic.port}")
    private Integer elasticPort;

    private JestClientFactory factory;

    private static final Logger log = LoggerFactory.getLogger(ElasticClientService.class);

    private JestClientFactory getFactory() {
        if (factory == null) {
            factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig
                    .Builder(elasticUrl + ":" + elasticPort)
                    .multiThreaded(true)
                    //Per default this implementation will create no more than 2 concurrent connections per given route
                    .defaultMaxTotalConnectionPerRoute(2)
                    // and no more 20 connections in total
                    .maxTotalConnection(10)
                    .build());
        }
        return factory;
    }

    public void indexDocument(String id, Object elasticObject, String index, String type) throws IOException {
        JestClient client = getFactory().getObject();
        Index document = new Index.Builder(elasticObject).index(index).type(type).id(id).build();
        client.execute(document);
    }

    public void deleteDocument(String id, String index, String type) throws IOException {
        JestClient client = getFactory().getObject();
        Delete delete =  new Delete.Builder(id).index(index).type(type).build();
        client.execute(delete);
    }
}
