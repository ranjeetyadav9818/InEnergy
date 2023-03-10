package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.PdpSrNotification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface PdpSrNotificationDao extends Repository<PdpSrNotification, String> {
    @Query("SELECT n FROM PdpSrNotification n WHERE n.creationTimestamp < ?1 AND n.voiceRecordFileName IS NOT NULL")
    List<PdpSrNotification> findOldVoiceNotifications(Date date);
}