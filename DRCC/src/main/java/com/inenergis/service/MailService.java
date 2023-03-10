package com.inenergis.service;

import com.inenergis.util.MailUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Session;
import java.util.Map;

@Stateless
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Resource(name = "java:jboss/mail/gmail")
    private Session session;

    private MailUtil mailUtil;

    public boolean sendPlainHTML(String addresses, String topic, String textMessage, String from) {
        generateMailUtil();
        return mailUtil.sendPlainHTML(addresses,topic,textMessage, from);
    }

    public boolean sendTemplate(String to, String from, String templateBodyName, String templateSubjectName, Map<String, Object> values) {
        generateMailUtil();
        return mailUtil.sendTemplate(to, from, templateBodyName, templateSubjectName,values);
    }

    public MailUtil generateMailUtil(){
        if(mailUtil == null){
            mailUtil = new MailUtil(session);
        }
        return mailUtil;
    }
}
