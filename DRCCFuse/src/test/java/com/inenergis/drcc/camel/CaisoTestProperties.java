package com.inenergis.drcc.camel;

import java.util.Properties;


public class CaisoTestProperties {

    public static Properties getPropertiesForTestingCaiso(){
        Properties properties = new Properties();
        properties.setProperty("caiso.retrieveLocation.url", "https://wsmap.caiso.com/sst/drrs/RetrieveDRLocations_DRRSv1_AP");
        properties.setProperty("caiso.retrieveRegistration.url", "https://wsmap.caiso.com/sst/drrs/RetrieveDRRegistrations_DRRSv1_AP");
        properties.setProperty("caiso.retrieveBatch.url", "https://wsmap.caiso.com/sst/drrs/RetrieveBatchValidationStatus_DRRSv1_AP");
        properties.setProperty("caiso.submitLocation.url", "https://wsmap.caiso.com/sst/drrs/SubmitDRLocations_DRRSv1_AP");
        properties.setProperty("caiso.submitRegistration.url", "https://wsmap.caiso.com/sst/drrs/SubmitDRRegistrations_DRRSv1_AP");
        properties.setProperty("caiso.keystore.location", "DRCC SYSTEMx5746.jks");
        properties.setProperty("caiso.keystore.password", "F7Y4nw9Egd5t");
        properties.setProperty("caiso.keystore.type", "JKS");
        properties.setProperty("caiso.keystore.alias", "te-ed444cb9-64e5-402b-bfa4-6ad77a6eda48");
        properties.setProperty("caiso.security.header","CN=DRCC SYSTEMx5746,OU=people,O=CAISO,C=US");
        properties.setProperty("caiso.keystore.properties.file", "clientKeystore.properties");
        properties.setProperty("caiso.ads.url", "https://adsmap.caiso.com:447/ADS/APIWebService/v7");
        return properties;
    }
}
