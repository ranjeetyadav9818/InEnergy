package com.inenergis.entity.workflow;

import com.inenergis.commonServices.JMSUtilContract;

import javax.jms.JMSException;
import java.util.Properties;

public class SystemTaskExecutor {

    public static void execute(TaskInstance taskInstance, Properties properties, JMSUtilContract jMSUtil) throws JMSException {
        switch (taskInstance.getTask().getName()) {
            case "ISO Location Registration":
                startLocationRegistration(taskInstance, properties, jMSUtil);
                break;
            case "ISO Location end date":
                startLocationUnregistering(taskInstance, properties, jMSUtil);
                break;
        }
    }

    private static void startLocationRegistration(TaskInstance taskInstance, Properties properties, JMSUtilContract jMSUtil) throws JMSException {
        final ProgramPlanInstance planInstance = (ProgramPlanInstance) taskInstance.getPlanInstance();
        final String textMessage = Long.toString(planInstance.getProgramServiceAgreementEnrollment().getId());
        sendJMSMessage(textMessage, properties.getProperty("location.enrollment"),jMSUtil);
    }

    private static void startLocationUnregistering(TaskInstance taskInstance, Properties properties, JMSUtilContract jMSUtil) throws JMSException {
        final ProgramPlanInstance planInstance = (ProgramPlanInstance) taskInstance.getPlanInstance();
        final String textMessage = Long.toString(planInstance.getProgramServiceAgreementEnrollment().getId());
        sendJMSMessage(textMessage, properties.getProperty("location.unenrollment"), jMSUtil);
    }

    private static void sendJMSMessage(String textMessage, String queueName, JMSUtilContract jMSUtil) throws JMSException {
        jMSUtil.sendMessage(textMessage, queueName);
    }
}
