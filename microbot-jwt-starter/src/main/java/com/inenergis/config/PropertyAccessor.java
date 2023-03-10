package com.inenergis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by egamas on 12/10/2017.
 */
@Getter
@Setter
@Component
@Configuration
public class PropertyAccessor {

    @Value("${pge.api.username}")
    private String username;
    @Value("${pge.api.password}")
    private String password;
    @Value("${pge.api.url}")
    private String url;
    @Value("${pge.api.numberOfDaysToCheck}")
    private String numberOfDaysToCheck;
    @Value("${pge.api.availableDaysToBeReady}")
    private String availableDaysToBeReady;
    @Value("${pge.api.peakdemand.url}")
    private String peakdemandUrl;
    @Value("${pge.api.resourceregistration.url}")
    private String resourceregistration;
    @Value("${pge.api.bid.url}")
    private String bid;
    @Value("${pge.api.bidstatus.url}")
    private String bidstatus;

    //Old JMS properties todo delete
    @Value("${jms.host}")
    private String jmsHostUrl;
    @Value("${jms.username}")
    private String jmsUsername;
    @Value("${jms.password}")
    private String jmsPassword;

    //New JMS properties
    @Value("${location.enrollment.default}")
    private String enrollmentQueue;
    @Value("${location.enrollment.deadletter}")
    private String enrollmentQueueDeadletter;
    @Value("${location.unenrollment.deadletter}")
    private String unenrollmentQueueDeadletter;
    @Value("${location.unenrollment.default}")
    private String unenrollmentQueue;
    @Value("${location.reenrollment.default}")
    private String reenrollmentQueue;
    @Value("${location.reenrollment.deadletter}")
    private String reenrollmentQueueDeadletter;

    @Bean(name = "appProperties")
    @Qualifier("appProperties")
    public Properties appProperties() {
        Properties properties = new Properties();

        //Layer7

        properties.put("pge.api.username", username);
        properties.put("pge.api.password", password);
        properties.put("pge.api.url", url);
        properties.put("pge.api.numberOfDaysToCheck", numberOfDaysToCheck);
        properties.put("pge.api.availableDaysToBeReady", availableDaysToBeReady);
        properties.put("pge.api.peakdemand.url", peakdemandUrl);
        properties.put("pge.api.resourceregistration.url", resourceregistration);
        properties.put("pge.api.bid.url", bid);
        properties.put("pge.api.bidstatus.url", bidstatus);

        //JMS old todo delete
        properties.put("jms.host", jmsHostUrl);
        properties.put("jms.username", jmsUsername);
        properties.put("jms.password", jmsPassword);

        //JMS new
        properties.put("location.enrollment", enrollmentQueue);
        properties.put("location.enrollment.deadletter", enrollmentQueueDeadletter);
        properties.put("location.unenrollment.deadletter", unenrollmentQueueDeadletter);
        properties.put("location.unenrollment", unenrollmentQueue);
        properties.put("location.reenrollment", reenrollmentQueue);
        properties.put("location.reenrollment.deadletter", reenrollmentQueueDeadletter);

        return properties;
    }
}
