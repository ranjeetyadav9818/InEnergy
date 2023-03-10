package com.inenergis.microbot.camel.services;

import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.entity.workflow.BusinessOwner;
import com.inenergis.entity.workflow.NotificationDefinition;
import com.inenergis.entity.workflow.NotificationInstance;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.microbot.camel.dao.NotificationDefinitionDao;
import com.inenergis.microbot.camel.dao.NotificationInstanceDao;
import com.inenergis.util.MailUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@Service
public class MailNotificationService {

    @Autowired
    private javax.mail.Session mailSession;
    @Autowired
    private NotificationInstanceDao notificationInstanceDao;
    @Autowired
    private NotificationDefinitionDao notificationDefinitionDao;
    @Autowired
    @Qualifier("appProperties")
    private Properties properties;

    public void retrievePendingNotifications(Exchange exchange) {
        final List<NotificationInstance> notSentNotifications = notificationInstanceDao.findBySent(false);
        if (!CollectionUtils.isEmpty(notSentNotifications)) {
            //If business owner is null retrieve from notification definition
            List<NotificationDefinition> notificationDefinitions = notificationDefinitionDao.findAll();
            Map<NotificationDefinitionId, BusinessOwner> map = (new WorkflowEngine()).getNotificationDefinitionIdBusinessOwnerMap(notificationDefinitions);
            for (NotificationInstance notification : notSentNotifications) {
                if (notification.getBusinessOwner() == null) {
                    notification.setBusinessOwner(map.get(notification.getType()));
                }
            }
        }
        exchange.getIn().setBody(notSentNotifications);
    }

    public void sendNotification(Exchange exchange) throws IOException {
        final NotificationInstance notification = exchange.getIn().getBody(NotificationInstance.class);
        MailUtil mailUtil = new MailUtil(mailSession);
        final BusinessOwner businessOwner = notification.getBusinessOwner();
        if (businessOwner != null && !StringUtils.isEmpty(businessOwner.getEmailList())) {
            final String[] mails = businessOwner.getEmailList().trim().split(",");
            boolean sent = true; //first time true. If all sent it turned out to be true
            for (String mail : mails) {
                final String header = StringUtils.defaultIfEmpty(notification.getMessageHeader(), StringUtils.EMPTY);
                final String body = StringUtils.defaultIfEmpty(notification.getMessageBody(), StringUtils.EMPTY);
                sent &= mailUtil.sendPlainHTML(mail, header, body, properties.getProperty("mail.user"));
            }
            notification.setSent(sent);
        }
    }
}