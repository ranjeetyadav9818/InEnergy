package com.inenergis.entity.workflow;


import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.util.VelocityUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
public class Alert {

    private NotificationDefinitionId type;
    private BusinessOwner businessOwner;
    private Map<String, Object> messageFields;
    private String reference;

    public NotificationInstance generateNotification(VelocityUtil velocityUtil) {

        final Pair pair = velocityUtil.renderTemplate(type.getMailBodyTemplate().getText(), type.getMailHeaderTemplate().getText(), getMessageFields());

        final NotificationInstance newNotification = NotificationInstance.builder()
                .type(type)
                .businessOwner(businessOwner)
                .created(new Date())
                .reference(reference)
                .messageHeader((String) pair.getLeft())
                .messageBody((String) pair.getRight())
                .build();

        return newNotification;
    }
}
