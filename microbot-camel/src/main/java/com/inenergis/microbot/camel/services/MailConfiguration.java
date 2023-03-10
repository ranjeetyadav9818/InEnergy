package com.inenergis.microbot.camel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${mail.user}")
    private String mailUser;
    @Value("${mail.password}")
    private String mailPassword;

    @Autowired
    @Qualifier("appProperties")
    private Properties properties;

    @Bean(name = "mailSession")
    public javax.mail.Session generateSession() throws IOException {
        return Session.getInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUser, mailPassword);
                    }
                });
    }
}
