package com.inenergis.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.FileInputStream;
import java.util.Properties;

@Singleton
@Startup
public class PropertyAccessor {

    private static final Logger log = LoggerFactory.getLogger(PropertyAccessor.class);
    private Properties properties = new Properties();

    @PostConstruct
    public void configure() {
        String fileName = System.getProperty("jboss.server.config.dir") + "/drcc.properties";
        try {
            FileInputStream fis = new FileInputStream(fileName);
            properties.load(fis);
        } catch (Exception e) {
            log.error("error loading the properties with filename " + fileName, e);
        }
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }
}