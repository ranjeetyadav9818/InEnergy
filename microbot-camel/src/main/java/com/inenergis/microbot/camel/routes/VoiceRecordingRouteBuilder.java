package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.beans.NotificationIdFinder;
import com.inenergis.microbot.camel.csv.RecordingManifest;
import com.inenergis.microbot.camel.processors.AmazonDeleteProcessor;
import com.inenergis.microbot.camel.processors.AmazonUploadProcessor;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class VoiceRecordingRouteBuilder extends RouteBuilder {

    private static final String SQL_DELETE_NOTIFICATION_BY_ID = "sql:UPDATE PDP_SR_NOTIFICATIONS SET VOICE_FILE_NAME = NULL WHERE NOTIFICATION_ID=:#${body.notificationId}";

    private NotificationIdFinder notificationIdFinder = new NotificationIdFinder();

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat manifestBinder = new BindyCsvDataFormat(RecordingManifest.class);

        if (!ConstantsProviderModel.TRUE.equals(appProperties.getProperty("route.enableVoice"))) {
            log.info(" VoiceRecordingRouteBuilder disabled ");
            return;
        } else {
            log.info(" VoiceRecordingRouteBuilder enabled ");
        }

        //First step download the recordings from ftp
        String ftpUrl = appProperties.getProperty("ftp.incoming.voicefiles.url");
        if (StringUtils.isNoneBlank(ftpUrl)) {
            String ftpUser = appProperties.getProperty("ftp.incoming.voicefiles.user");
            String ftpPassword = appProperties.getProperty("ftp.incoming.voicefiles.password");
            String ftpFullUrl = String.format("%s?filter=#recordingsFileFilter&localWorkDirectory=work/drcc/recordings/tmp&username=%s&password=%s&download=true&delete=true&recursive=true&scheduler=quartz2&scheduler.cron=0+38+1-23+?+*+MON-SUN+*", ftpUrl, ftpUser, ftpPassword);
            from(ftpFullUrl)
                    .to("file:work/drcc/recordings/?fileName=${file:onlyname}");
        }

        //Second step unzip the received file
        from("file:work/drcc/recordings/?delete=true").id("voiceRecordingUnzipper")
                .log("unzipping voice file: ${file:onlyname}")
                .split(new ZipSplitter()).streaming()
                .to("file:work/drcc/recordings/unzip/?fileName=${file:onlyname.noext}/${header.zipFileName}")
                .end().log("voice file unzipped");

        //3rd step reading manifest file
        from("file:work/drcc/recordings/unzip?delete=true&recursive=true&fileName=manifest.csv").id("voiceRecordingManifestReader")
                .split(body().tokenize("\n")).streaming()
                .choice()
                .when(simple("${body} contains 'SuperEnterpriseID'"))
                .log("Skipping first line")
                .endChoice()
                .otherwise()
                .to("seda:processRecordingsManifest?size=40&concurrentConsumers=20&blockWhenFull=true")
                .endChoice();
        from("seda:processRecordingsManifest?size=40&concurrentConsumers=20&blockWhenFull=true").id("voiceRecordingDatabaseWriter")
                .unmarshal(manifestBinder)
                .split(body())
                .bean(notificationIdFinder, "findNotificationId")
                .setHeader("voiceFileName", simple("${body.fileName}"))
                .to("sql:UPDATE PDP_SR_NOTIFICATIONS SET VOICE_FILE_NAME=:#voiceFileName WHERE NOTIFICATION_ID=:#drccNotificationId")
                .log("${header.voiceFileName} has been assigned to ${header.drccNotificationId}")
                .setBody(constant(""))
                .to("file:?fileName=${file:parent}/${headers.voiceFileName}.done");

        //4th step upload to S3
        String accessKey = appProperties.getProperty("aws.voicefiles.accesskey");
        String secretKey = appProperties.getProperty("aws.voicefiles.secretkey");
        String bucket = appProperties.getProperty("aws.voicefiles.bucket");
        AmazonUploadProcessor amazonUploadProcessor = new AmazonUploadProcessor(accessKey, secretKey, bucket);
        from("file:work/drcc/recordings/unzip/?delete=true&recursive=true&doneFileName=${file:name}.done").id("voiceRecordingAmazonSender").process(amazonUploadProcessor);

        //5th step clean old voice files from local and s3
        AmazonDeleteProcessor amazonDeleteProcessor = new AmazonDeleteProcessor(accessKey, secretKey, bucket);
        from("quartz2://onceADay?cron=0+0+11+?+*+*").id("oldVoiceRecordingsFinder").bean(notificationIdFinder, "findOldNotificationIds")
                .split(simple("header.old_notifications")).to("seda:deleteVoiceFiles?size=40&concurrentConsumers=20&blockWhenFull=true");


        from("seda:deleteVoiceFiles?size=40&concurrentConsumers=20&blockWhenFull=true").id("voiceRecordingsCleaner")
                .process(amazonDeleteProcessor).to(SQL_DELETE_NOTIFICATION_BY_ID);
    }
}