package com.inenergis.microbot.camel.beans;

import java.io.Serializable;

public class PdpSrNotification implements Serializable {

    private String notificationId;
    private String voiceFileName;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }

    public void setVoiceFileName(String voiceFileName) {
        this.voiceFileName = voiceFileName;
    }
}
