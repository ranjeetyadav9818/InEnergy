package com.inenergis.microbot.camel.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Getter
public class ConfigurationService {

    //DRCC PROPERTIES

    @Value("${database.url}")
    private String databaseUrl;
    @Value("${database.user}")
    private String databaseUser;
    @Value("${database.password}")
    private String databasePassword;
    @Value("${ftp.incoming.url}")
    private String ftpIncomingUrl;
    @Value("${ftp.incoming.user}")
    private String ftpIncomingUser;
    @Value("${ftp.incoming.password}")
    private String ftpIncomingPassword;
    @Value("${ftp.incoming.voicefiles.url}")
    private String ftpIncomingVoicefilesUrl;
    @Value("${ftp.incoming.voicefiles.user}")
    private String ftpIncomingVoicefilesUser;
    @Value("${ftp.incoming.voicefiles.password}")
    private String ftpIncomingVoicefilesPassword;
    @Value("${aws.voicefiles.accesskey}")
    private String awsVoicefilesAccesskey;
    @Value("${aws.voicefiles.secretkey}")
    private String awsVoicefilesSecretkey;
    @Value("${aws.voicefiles.bucket}")
    private String awsVoicefilesBucket;
    @Value("${ftp.incoming.cdw.url}")
    private String ftpIncomingCdwUrl;
    @Value("${ftp.incoming.cdw.newUrl}")
    private String ftpIncomingCdwNewUrl;
    @Value("${ftp.incoming.cdw.user}")
    private String ftpIncomingCdwUser;
    @Value("${ftp.incoming.cdw.password}")
    private String ftpIncomingCdwPassword;
    @Value("${ftp.incoming.preferences.url}")
    private String ftpIncomingPreferencesUrl;
    @Value("${ftp.incoming.preferences.user}")
    private String ftpIncomingPreferencesUser;
    @Value("${ftp.incoming.preferences.password}")
    private String ftpIncomingPreferencesPassword;
    @Value("${pge.api.username}")
    private String pgeApiUsername;
    @Value("${pge.api.password}")
    private String pgeApiPassword;
    @Value("${ftp.incoming.trove.url}")
    private String ftpIncomingTroveUrl;
    @Value("${ftp.incoming.trove.user}")
    private String ftpIncomingTroveUser;
    @Value("${ftp.incoming.trove.password}")
    private String ftpIncomingTrovePassword;
    @Value("${jms.host}")
    private String jmsHost;
    @Value("${jms.username}")
    private String jmsUsername;
    @Value("${jms.password}")
    private String jmsPassword;
    @Value("${pge.certificate.location}")
    private String pgeCertificateLocation;
    @Value("${pge.certificate.password}")
    private String pgeCertificatePassword;
    @Value("${pge.api.url}")
    private String pgeApiUrl;
    @Value("${pge.api.numberOfDaysToCheck}")
    private String pgeApiNumberOfDaysToCheck;
    @Value("${pge.api.availableDaysToBeReady}")
    private String pgeApiAvailableDaysToBeReady;
    @Value("${pge.api.peakdemand.url}")
    private String pgeApiPeakdemandUrl;
    @Value("${pge.api.resourceregistration.url}")
    private String pgeApiResourceregistrationUrl;
    @Value("${pge.api.bid.url}")
    private String pgeApiBidUrl;
    @Value("${pge.api.bidstatus.url}")
    private String pgeApiBidstatusUrl;
    @Value("${caiso.retrieveLocation.url}")
    private String caisoRetrieveLocationUrl;
    @Value("${caiso.retrieveRegistration.url}")
    private String caisoRetrieveRegistrationUrl;
    @Value("${caiso.retrieveBatch.url}")
    private String caisoRetrieveBatchUrl;
    @Value("${caiso.submitLocation.url}")
    private String caisoSubmitLocationUrl;
    @Value("${caiso.submitRegistration.url}")
    private String caisoSubmitRegistrationUrl;
    @Value("${caiso.keystore.location}")
    private String caisoKeystoreLocation;
    @Value("${caiso.keystore.password}")
    private String caisoKeystorePassword;
    @Value("${caiso.keystore.type}")
    private String caisoKeystoreType;
    @Value("${caiso.keystore.alias}")
    private String caisoKeystoreAlias;
    @Value("${caiso.security.header}")
    private String caisoSecurityHeader;
    @Value("${caiso.keystore.properties.file}")
    private String caisoKeystorePropertiesFile;
    @Value("${pge.api.weather.forecast.url}")
    private String pgeApiWeatherForecastUrl;
    @Value("${caiso.ads.url}")
    private String caisoAdsUrl;
    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
    @Value("${mail.smtp.port}")
    private String mailSmtpPort;
    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;
    @Value("${mail.smtp.socketFactory.port}")
    private String mailSmtpSocketFactoryPort;
    @Value("${mail.smtp.socketFactory.class}")
    private String mailSmtpSocketFactoryClass;
    @Value("${mail.smtp.socketFactory.fallback}")
    private String mailSmtpSocketFactoryFallback;
    @Value("${mail.user}")
    private String mailUser;
    @Value("${mail.password}")
    private String mailPassword;
    @Value("${route.enableVoice}")
    private String routeEnableVoice;
    @Value("${route.enablePGECdw}")
    private String routeEnablePGECdw;
    @Value("${route.enableWeatherForecastRoute}")
    private String routeEnableWeatherForecastRoute;
    @Value("${route.enableNewCustomerCdw}")
    private String routeEnableNewCustomerCdw;

    // New properties (microservices)
    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.profiles.active}")
    private String springProfilesActive;
    @Value("${mysql.jdbc.url}")
    private String mysqlJdbcUrl;
    @Value("${mysql.jdbc.user}")
    private String mysqlJdbcUser;
    @Value("${mysql.jdbc.pass}")
    private String mysqlJdbcPass;
    @Value("${mysql.jdbc.driverClassName}")
    private String mysqlJdbcDriverClassName;
    @Value("${mysql.hibernate.dialect}")
    private String mysqlHibernateDialect;
    @Value("${mysql.hibernate.show_sql}")
    private String mysqlHibernateShowSql;
    @Value("${elastic.url}")
    private String elasticUrl;
    @Value("${elastic.port}")
    private String elasticPort;
    @Value("${location.enrollment.deadletter}")
    private String locationEnrollmentDeadletter;
    @Value("${location.enrollment}")
    private String location_enrollment;
    @Value("${location.unenrollment.deadletter}")
    private String locationUnenrollmentDeadletter;
    @Value("${location.unenrollment}")
    private String location_unenrollment;
    @Value("${location.reenrollment.deadletter}")
    private String locationReenrollmentDeadletter;
    @Value("${location.reenrollment}")
    private String locationReenrollment;
    @Value("${jms.queue.name}")
    private String jmsQueueName;
    @Value("${award.enabled}")
    private String awardEnabled;

    @Bean(name = "appProperties")
    @Qualifier("appProperties")
    public Properties appProperties() {
        Properties properties = new Properties();
        properties.setProperty("database.url", databaseUrl);
        properties.setProperty("database.user", databaseUser);
        properties.setProperty("database.password", databasePassword);
        properties.setProperty("ftp.incoming.url", ftpIncomingUrl);
        properties.setProperty("ftp.incoming.user", ftpIncomingUser);
        properties.setProperty("ftp.incoming.password", ftpIncomingPassword);
        properties.setProperty("ftp.incoming.voicefiles.url", ftpIncomingVoicefilesUrl);
        properties.setProperty("ftp.incoming.voicefiles.user", ftpIncomingVoicefilesUser);
        properties.setProperty("ftp.incoming.voicefiles.password", ftpIncomingVoicefilesPassword);
        properties.setProperty("aws.voicefiles.accesskey", awsVoicefilesAccesskey);
        properties.setProperty("aws.voicefiles.secretkey", awsVoicefilesSecretkey);
        properties.setProperty("aws.voicefiles.bucket", awsVoicefilesBucket);
        properties.setProperty("ftp.incoming.cdw.url", ftpIncomingCdwUrl);
        properties.setProperty("ftp.incoming.cdw.newUrl", ftpIncomingCdwNewUrl);
        properties.setProperty("ftp.incoming.cdw.user", ftpIncomingCdwUser);
        properties.setProperty("ftp.incoming.cdw.password", ftpIncomingCdwPassword);
        properties.setProperty("ftp.incoming.preferences.url", ftpIncomingPreferencesUrl);
        properties.setProperty("ftp.incoming.preferences.user", ftpIncomingPreferencesUser);
        properties.setProperty("ftp.incoming.preferences.password", ftpIncomingPreferencesPassword);
        properties.setProperty("pge.api.username", pgeApiUsername);
        properties.setProperty("pge.api.password", pgeApiPassword);
        properties.setProperty("ftp.incoming.trove.url", ftpIncomingTroveUrl);
        properties.setProperty("ftp.incoming.trove.user", ftpIncomingTroveUser);
        properties.setProperty("ftp.incoming.trove.password", ftpIncomingTrovePassword);
        properties.setProperty("jms.host", jmsHost);
        properties.setProperty("jms.username", jmsUsername);
        properties.setProperty("jms.password", jmsPassword);
        properties.setProperty("pge.certificate.location", pgeCertificateLocation);
        properties.setProperty("pge.certificate.password", pgeCertificatePassword);
        properties.setProperty("pge.api.url", pgeApiUrl);
        properties.setProperty("pge.api.numberOfDaysToCheck", pgeApiNumberOfDaysToCheck);
        properties.setProperty("pge.api.availableDaysToBeReady", pgeApiAvailableDaysToBeReady);
        properties.setProperty("pge.api.peakdemand.url", pgeApiPeakdemandUrl);
        properties.setProperty("pge.api.resourceregistration.url", pgeApiResourceregistrationUrl);
        properties.setProperty("pge.api.bid.url", pgeApiBidUrl);
        properties.setProperty("pge.api.bidstatus.url", pgeApiBidstatusUrl);
        properties.setProperty("caiso.retrieveLocation.url", caisoRetrieveLocationUrl);
        properties.setProperty("caiso.retrieveRegistration.url", caisoRetrieveRegistrationUrl);
        properties.setProperty("caiso.retrieveBatch.url", caisoRetrieveBatchUrl);
        properties.setProperty("caiso.submitLocation.url", caisoSubmitLocationUrl);
        properties.setProperty("caiso.submitRegistration.url", caisoSubmitRegistrationUrl);
        properties.setProperty("caiso.keystore.location", caisoKeystoreLocation);
        properties.setProperty("caiso.keystore.password", caisoKeystorePassword);
        properties.setProperty("caiso.keystore.type", caisoKeystoreType);
        properties.setProperty("caiso.keystore.alias", caisoKeystoreAlias);
        properties.setProperty("caiso.security.header", caisoSecurityHeader);
        properties.setProperty("caiso.keystore.properties.file", caisoKeystorePropertiesFile);
        properties.setProperty("pge.api.weather.forecast.url", pgeApiWeatherForecastUrl);
        properties.setProperty("caiso.ads.url", caisoAdsUrl);
        properties.setProperty("mail.smtp.host", mailSmtpHost);
        properties.setProperty("mail.smtp.port", mailSmtpPort);
        properties.setProperty("mail.smtp.auth", mailSmtpAuth);
        properties.setProperty("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        properties.setProperty("mail.smtp.socketFactory.port", mailSmtpSocketFactoryPort);
        properties.setProperty("mail.smtp.socketFactory.class", mailSmtpSocketFactoryClass);
        properties.setProperty("mail.smtp.socketFactory.fallback", mailSmtpSocketFactoryFallback);
        properties.setProperty("mail.user", mailUser);
        properties.setProperty("mail.password", mailPassword);
        properties.setProperty("route.enableVoice", routeEnableVoice);
        properties.setProperty("route.enablePGECdw", routeEnablePGECdw);
        properties.setProperty("route.enableWeatherForecastRoute", routeEnableWeatherForecastRoute);
        properties.setProperty("route.enableNewCustomerCdw", routeEnableNewCustomerCdw);

        // new properties

        properties.setProperty("server.port", serverPort);
        properties.setProperty("spring.profiles.active", springProfilesActive);
        properties.setProperty("mysql.jdbc.url", mysqlJdbcUrl);
        properties.setProperty("mysql.jdbc.user", mysqlJdbcUser);
        properties.setProperty("mysql.jdbc.pass", mysqlJdbcPass);
        properties.setProperty("mysql.jdbc.driverClassName", mysqlJdbcDriverClassName);
        properties.setProperty("mysql.hibernate.dialect", mysqlHibernateDialect);
        properties.setProperty("mysql.hibernate.show_sql", mysqlHibernateShowSql);
        properties.setProperty("elastic.url", elasticUrl);
        properties.setProperty("elastic.port", elasticPort);
        properties.setProperty("location.enrollment.deadletter", locationEnrollmentDeadletter);
        properties.setProperty("location.enrollment", location_enrollment);
        properties.setProperty("location.unenrollment.deadletter", locationUnenrollmentDeadletter);
        properties.setProperty("location.unenrollment", location_unenrollment);
        properties.setProperty("location.reenrollment.deadletter", locationReenrollmentDeadletter);
        properties.setProperty("location.reenrollment", locationReenrollment);
        properties.setProperty("jms.queue.name", jmsQueueName);
        properties.setProperty("award.enabled", awardEnabled);
        return properties;
    }
}