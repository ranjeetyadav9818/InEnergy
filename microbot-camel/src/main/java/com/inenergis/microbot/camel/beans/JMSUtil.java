package com.inenergis.microbot.camel.beans;


import com.inenergis.commonServices.JMSUtilContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@Component
public class JMSUtil implements JMSUtilContract {

    @Autowired
    private JmsTemplate defaultJmsTemplate;

    @Override
    public void sendMessage(String textMessage, String queueName) {
        defaultJmsTemplate.convertAndSend(queueName, textMessage);

    }
}