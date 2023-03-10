package com.inenergis.oneShot;

import com.inenergis.oneShot.service.AgreementPointMapService;
import com.inenergis.oneShot.service.ElasticAggregatorService;
import com.inenergis.oneShot.service.ElasticAssetService;
import com.inenergis.oneShot.service.ElasticContractService;
import com.inenergis.oneShot.service.ElasticDeviceService;
import com.inenergis.oneShot.service.ElasticEntityService;
import com.inenergis.oneShot.service.ElasticEventNotificationService;
import com.inenergis.oneShot.service.ElasticEventService;
import com.inenergis.oneShot.service.ElasticInvoiceService;
import com.inenergis.oneShot.service.ElasticIsoService;
import com.inenergis.oneShot.service.ElasticLocationService;
import com.inenergis.oneShot.service.ElasticProgramService;
import com.inenergis.oneShot.service.ElasticRegistrationService;
import com.inenergis.oneShot.service.ElasticResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.Date;

@Component
public class ScheduledTasks {

    @Value("${elastic.url}")
    private String elasticUrl;
    @Value("${elastic.port}")
    private Integer elasticPort;

    @Autowired
    private AgreementPointMapService apmService;
    @Autowired
    private ElasticAggregatorService aggregatorService;
    @Autowired
    private ElasticIsoService elasticIsoService;
    @Autowired
    private ElasticAssetService elasticAssetService;
    @Autowired
    private ElasticDeviceService elasticDeviceService;
    @Autowired
    private ElasticResourceService elasticResourceService;
    @Autowired
    private ElasticLocationService elasticLocationService;
    @Autowired
    private ElasticRegistrationService elasticRegistrationService;
    @Autowired
    private ElasticProgramService elasticProgramService;
    @Autowired
    private ElasticEntityService elasticEntityService;
    @Autowired
    private ElasticContractService elasticContractService;
    @Autowired
    private ElasticEventNotificationService elasticEventNotificationService;
    @Autowired
    private ElasticEventService elasticEventService;
    @Autowired
    private ElasticInvoiceService elasticInvoiceService;

    private static final long TEN_DAYS_IN_MILLIS = 864_000_000;
    private static final int BATCH_SIZE = 1_000;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private JestClientFactory factory;

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void apmPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (apmService.sendAPMToElastic(client, i, BATCH_SIZE)) {
            log.info("APM sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void aggregatorPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (aggregatorService.sendAggregatorsToElastic(client, i, BATCH_SIZE)) {
            log.info("Aggregators sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void isoPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticIsoService.sendIsoToElastic(client, i, BATCH_SIZE)) {
            log.info("Isos sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void assetPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticAssetService.sendAssetToElastic(client, i, BATCH_SIZE)) {
            log.info("Assets sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void devicePopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticDeviceService.sendDevicesToElastic(client, i, BATCH_SIZE)) {
            log.info("Devices sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void resourcePopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticResourceService.sendResourcesToElastic(client, i, BATCH_SIZE)) {
            log.info("Resources sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void locationPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticLocationService.sendLocationsToElastic(client, i, BATCH_SIZE)) {
            log.info("Locations sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void registrationPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticRegistrationService.sendRegistrationsToElastic(client, i, BATCH_SIZE)) {
            log.info("Registrations sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void programPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticProgramService.sendProgramToElastic(client, i, BATCH_SIZE)) {
            log.info("Programs sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10) // Every 10 days
    public void contractEntityPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticEntityService.sendContractEntityToElastic(client, i, BATCH_SIZE)) {
            log.info("Contract entities sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10) // Every 10 days
    public void contractPopulator() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticContractService.sendContractToElastic(client, i, BATCH_SIZE)) {
            log.info("Contracts sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void eventNotificationPopulation() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticEventNotificationService.sendEventNotificationToElastic(client, i, BATCH_SIZE)) {
            log.info("Event notifications sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void eventPopulation() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticEventService.sendEventToElastic(client, i, BATCH_SIZE)) {
            log.info("Events sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

    @Scheduled(fixedDelay = TEN_DAYS_IN_MILLIS, initialDelay = 10)
    public void invoicePopulation() throws UnknownHostException, JsonProcessingException {
        JestClient client = getFactory().getObject();
        int i = 0;
        while (elasticInvoiceService.sendInvoiceToElastic(client, i, BATCH_SIZE)) {
            log.info("Invoices sent to ES: {} on {}", i * BATCH_SIZE, new Date());
            i++;
            client = factory.getObject();
        }
    }

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
}