package com.inenergis.microbot.camel.services;

import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Configuration
public class IniConfigurationService {

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext appContext;

    private static final String CDW_PATH = "classpath:/cdw";
    private static final String DRCC_PATH = "classpath:/drcc";

    @Bean(name = "drccIni")
    public Wini drcc() throws IOException {
        return getWini(DRCC_PATH);
    }

    @Bean(name = "cdwIni")
    public Wini cdw() throws IOException {
        return getWini(CDW_PATH);
    }

    private Wini getWini(String resource) throws IOException {
        return new Wini(appContext.getResource(resource + getProfileSuffix() + ".ini").getInputStream());
    }

    private String getProfileSuffix() {
        String suffix = "";
        if (env.getActiveProfiles().length > 0) {
            suffix = "-" + env.getActiveProfiles()[0];
        }
        return suffix;
    }
}