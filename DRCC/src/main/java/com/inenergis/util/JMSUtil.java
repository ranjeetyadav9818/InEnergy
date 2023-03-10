package com.inenergis.util;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.inenergis.commonServices.JMSUtilContract;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Session;

/**
 * Created by egamas on 23/10/2017.
 */
@Stateless
public class JMSUtil implements JMSUtilContract{

    @Inject
    PropertyAccessor propertyAccessor;

    private SQSConnectionFactory connectionFactory;

    private DefaultJmsListenerContainerFactory factory;

    JmsTemplate defaultJmsTemplate;

    public void init() {
        connectionFactory =
                SQSConnectionFactory.builder()
                        .withRegion(Region.getRegion(Regions.fromName(propertyAccessor.getProperties().getProperty("aws.region"))))
                        .withAWSCredentialsProvider(new DefaultAWSCredentialsProviderChain())
                        .build();

        factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("3-10");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setReceiveTimeout(1200_000L);

        defaultJmsTemplate = new JmsTemplate(this.connectionFactory);
    }

    @Override
    public void sendMessage(String textMessage, String queueName) {
        if (defaultJmsTemplate == null) {
            init();
        }
        defaultJmsTemplate.convertAndSend(queueName, textMessage);
    }
}
