package com.inenergis.microbot.camel.beans;

import com.inenergis.microbot.camel.csv.RecordingManifest;
import com.inenergis.microbot.camel.dao.PdpSrNotificationDao;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
public class NotificationIdFinder 	{

    @Autowired
    PdpSrNotificationDao pdpSrNotificationDao;

    private static final Logger log = LoggerFactory.getLogger(NotificationIdFinder.class);

    public void findNotificationId(@Body RecordingManifest manifest , @Headers Map headers) throws Exception {
        String notificationId = manifest.getClientID().split("/")[0];
        notificationId = notificationId.substring(0,notificationId.length()-2);
        headers.put("drccNotificationId",notificationId);
    }

    public void findOldNotificationIds(@Headers Map headers){
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault()).minusDays(45L);
        Date date = new Date(dateTime.toEpochSecond());
        headers.put("old_notifications",pdpSrNotificationDao.findOldVoiceNotifications(date));
    }
}
