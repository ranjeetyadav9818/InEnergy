package com.inenergis.microbot.camel.beans;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class DeadletterQueueListener {

    @Autowired
    JmsTemplate defaultJmsTemplate;
    @Value("${location.enrollment.deadletter}")
    private String enrollmentDeadletter;
    @Value("${location.enrollment}")
    private String enrollment;
    @Value("${location.unenrollment.deadletter}")
    private String unenrollmentDeadletter;
    @Value("${location.unenrollment}")
    private String unenrollment;
    @Value("${location.reenrollment.deadletter}")
    private String reenrollmentDeadletter;
    @Value("${location.reenrollment}")
    private String reenrollment;

    public void retrieveEnrollmentDeadLetter(){
        Object o = defaultJmsTemplate.receiveAndConvert(enrollmentDeadletter);
        if (o != null) {
            defaultJmsTemplate.convertAndSend(enrollment,o);
        }
    }

    public void retrieveUnenrollmentDeadLetter(){
        Object o = defaultJmsTemplate.receiveAndConvert(unenrollmentDeadletter);
        if (o != null) {
            defaultJmsTemplate.convertAndSend(unenrollment,o);
        }
    }

    public void retrieveReenrollmentDeadLetter(){
        Object o = defaultJmsTemplate.receiveAndConvert(reenrollmentDeadletter);
        if (o != null) {
            defaultJmsTemplate.convertAndSend(reenrollment,o);
        }
    }
}