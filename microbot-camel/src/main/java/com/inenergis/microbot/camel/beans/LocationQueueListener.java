package com.inenergis.microbot.camel.beans;


import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;
import java.time.Clock;

@Service
@Getter
@Setter
public class LocationQueueListener {

    public static final int TWO_SECONDS = 2000;
    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private JmsTemplate defaultJmsTemplate;

    @Value("${location.enrollment}")
    private String enrollmentQueue;

    @Value("${location.enrollment.deadletter}")
    private String enrollmentQueueDeadletter;

    @Value("${location.unenrollment.deadletter}")
    private String unenrollmentQueueDeadletter;

    @Value("${location.reenrollment}")
    private String reenrollmentQueue;

    @Value("${location.reenrollment.deadletter}")
    private String reenrollmentQueueDeadletter;

    @JmsListener(destination = "${location.enrollment}")
    public void enrollment(String saEnrollmentId) throws Exception {
        Thread.sleep(TWO_SECONDS);
        producerTemplate.sendBody("direct:locationEnrollment", saEnrollmentId);
    }
    @JmsListener(destination = "${location.unenrollment}")
    public void unenrollment(String saEnrollmentId) throws Exception {
        Thread.sleep(TWO_SECONDS);
        producerTemplate.sendBody("direct:locationUnenrollment", saEnrollmentId);
    }

    @JmsListener(destination = "${location.reenrollment}")
    public void reenrollment(String locationId) throws Exception {
        Thread.sleep(TWO_SECONDS);
        producerTemplate.sendBody("direct:locationReenrollment", locationId);
    }

    public void sendToEnrollmentDeadletter(Exchange exchange){
        defaultJmsTemplate.convertAndSend(enrollmentQueueDeadletter, exchange.getIn().getBody());
    }

    public void sendToUnenrollmentDeadletter(Exchange exchange){
        defaultJmsTemplate.convertAndSend(unenrollmentQueueDeadletter, exchange.getIn().getBody());
    }

    public void sendToReenrollmentDeadletter(Exchange exchange){
        defaultJmsTemplate.convertAndSend(reenrollmentQueueDeadletter, exchange.getIn().getBody());
    }

    public void sendToEnrollment(Exchange exchange){
        defaultJmsTemplate.convertAndSend(enrollmentQueue, exchange.getIn().getBody());
    }

    public void sendToReenrollment(Exchange exchange){
        defaultJmsTemplate.convertAndSend(reenrollmentQueue, exchange.getIn().getBody());
    }
}