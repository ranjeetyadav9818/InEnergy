package com.inenergis.commonServices;

/**
 * Created by egamas on 23/10/2017.
 */
public interface JMSUtilContract {
     void sendMessage(String textMessage, String queueName);
}
