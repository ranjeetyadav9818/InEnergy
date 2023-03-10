package com.inenergis.microbot.camel.services;

import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.entity.workflow.Alert;
import com.inenergis.entity.workflow.BusinessOwner;
import com.inenergis.entity.workflow.NotificationDefinition;
import com.inenergis.entity.workflow.NotificationInstance;
import com.inenergis.microbot.camel.dao.NotificationDefinitionDao;
import com.inenergis.util.VelocityUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class SystemAlertService { //It means crond generated alerts.

    protected abstract List searchForEntitiesToGenerateAlerts();

    public abstract Map<String, Object> buildMsgFields(Object entity);

    protected abstract String generateReference(Object entity);

    protected abstract NotificationDefinitionId getType();

    @Autowired
    private NotificationDefinitionDao notificationDefinitionDao;

    @Transactional
    protected BusinessOwner generateBusinessOwner(Object entity) {
        NotificationDefinition notificationDefinition = notificationDefinitionDao.getByType(getType());
        return notificationDefinition.getBusinessOwner();
    }

    public Alert generateAlert(Object entity) {
        return Alert.builder()
                .businessOwner(generateBusinessOwner(entity))
                .messageFields(buildMsgFields(entity))
                .reference(generateReference(entity)).type(getType()).build();
    }

    public void createNewAlerts(Exchange exchange) throws IOException {
        VelocityUtil velocityUtil = new VelocityUtil();

        List entities = searchForEntitiesToGenerateAlerts();
        List<NotificationInstance> newNotifications = new ArrayList<>(entities.size());
        if (!CollectionUtils.isEmpty(entities)) {
            for (Object entity : entities) {
                if (!CollectionUtils.isEmpty(entities)) {
                    Alert alert = generateAlert(entity);
                    final NotificationInstance notificationInstance = alert.generateNotification(velocityUtil);
                    newNotifications.add(notificationInstance);
                }
            }
            exchange.getIn().setBody(newNotifications);
        }
    }
}
