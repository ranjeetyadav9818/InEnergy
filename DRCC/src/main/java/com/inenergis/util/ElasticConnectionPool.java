package com.inenergis.util;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Created by Antonio on 23/08/2017.
 */
@Singleton
@Startup
public class ElasticConnectionPool {

    private JestClientFactory factory;

    @Inject
    PropertyAccessor propertyAccessor;

    @PostConstruct
    public void configure() {
        factory = new JestClientFactory();

        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(propertyAccessor.getValue("elastic.url")+":"+propertyAccessor.getValue("elastic.port"))
                .multiThreaded(true)
                //Per default this implementation will create no more than 2 concurrent connections per given route
                .defaultMaxTotalConnectionPerRoute(2)
                // and no more 20 connections in total
                .maxTotalConnection(10)
                .build());
    }

    public JestClient getClient(){
        return factory.getObject();
    }
}
