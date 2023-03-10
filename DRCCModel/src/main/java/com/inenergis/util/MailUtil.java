package com.inenergis.util;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;

public class MailUtil {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    private Session session;
    private VelocityUtil velocityUtil;

    public MailUtil(Session session) {
        this.velocityUtil = new VelocityUtil();
        this.session = session;
    }

    public boolean sendPlainHTML(String addresses, String topic, String textMessage, String from) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
            message.setSubject(topic);
            message.setText(textMessage, "utf-8", "html");
            message.setFrom(new InternetAddress(from));
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            log.warn("Cannot send mail", e);
            return false;
        }
    }

    public boolean sendTemplate(String to, String from,String templateBodyName, String templateSubjectName, Map<String, Object> values) {
        VelocityContext context = velocityUtil.buildVelocityContext(values);
        final String subject = velocityUtil.buildTemplate(templateSubjectName, context);
        final String body = velocityUtil.buildTemplate(templateBodyName, context);
        return sendPlainHTML(to, subject, body, from);
    }


}
