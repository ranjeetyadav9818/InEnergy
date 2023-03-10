package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.FileInformation;
import com.inenergis.microbot.camel.aggregator.MapMergeAggregationStrategy;
import com.inenergis.microbot.camel.aggregator.StringBodyAggregationStrategy;
import com.inenergis.microbot.camel.beans.EventService;
import com.inenergis.microbot.camel.beans.FileSorter;
import com.inenergis.microbot.camel.csv.Detail;
import com.inenergis.microbot.camel.csv.Notification;
import com.inenergis.microbot.camel.csv.Participant;
import com.inenergis.microbot.camel.csv.Postback;
import com.inenergis.microbot.camel.csv.Preference;
import com.inenergis.microbot.camel.processors.BodyInspector;
import com.inenergis.microbot.camel.processors.NotificationProcessor;
import com.inenergis.microbot.camel.processors.Pojo2Map;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.impl.ThreadPoolProfileSupport;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.spring.spi.ApplicationContextRegistry;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DrccRouteBuilder extends RouteBuilder {

    public static final String ADDRESS_DETAILS = "ADDRESS_DETAILS";
    public static final String CSV_LINE = "CSV_LINE";
    public static final String PREFERENCES_INSERT_OR_UPDATE_SQL = "sql:INSERT INTO CUSTOMER_NOTIFICATION_PREFERENCE(`UNIQ_SA_ID`,`SA_ID`,`PERSON_ID`,`NOTIFICATION_PROGRAM`,`NOTIFICATION_TYPE_ID`,`NOTIFICATION_VALUE`,`NOTIFICATION_LANGUAGE`,`NOTIFICATION_DNC_FLAG`,`START_DATE`,`END_DATE`,`PHONE_EXTENSION`) " +
            "VALUES(:#NOTIFICATION_OPTION_ID,:#SA_ID,:#PERSON_ID,:#NOTIFICATION_PROGRAM,:#NOTIFICATION_TYPE,:#NOTIFICATION_VALUE,:#NOTIFICATION_LANGUAGE,:#dncFlag,:#NOTIFICATION_EFF_STARTDT,:#NOTIFICATION_EFF_ENDDT,:#NOTIFICATION_VALUE_EXTENSION) " +
            "ON DUPLICATE KEY UPDATE SA_ID=:#SA_ID, PERSON_ID=:#PERSON_ID, NOTIFICATION_PROGRAM=:#NOTIFICATION_PROGRAM, NOTIFICATION_TYPE_ID=:#NOTIFICATION_TYPE,NOTIFICATION_VALUE=:#NOTIFICATION_VALUE, NOTIFICATION_LANGUAGE=:#NOTIFICATION_LANGUAGE, NOTIFICATION_DNC_FLAG=:#dncFlag, " +
            "START_DATE=:#NOTIFICATION_EFF_STARTDT, END_DATE=:#NOTIFICATION_EFF_ENDDT, PHONE_EXTENSION=:#NOTIFICATION_VALUE_EXTENSION";

    private boolean useIncomingFtp = true;
    private boolean useIncomingPostbackFtp = true;
    private boolean enablePreferenceParsing = true;

    @Autowired
    @Qualifier("appProperties")
    private Properties p;

    @Autowired
    private Wini drccIni;

    @Autowired
    private EventService eventService;

    @Override
    public void configure() throws Exception {
        //We have an OSGI registry that doesn't allow adding beans from java registerBeans();
        BindyCsvDataFormat bindyDetail = new BindyCsvDataFormat(Detail.class);
        BindyCsvDataFormat bindyNotification = new BindyCsvDataFormat(Notification.class);
        BindyCsvDataFormat bindyParticipant = new BindyCsvDataFormat(Participant.class);
        BindyCsvDataFormat bindyPostback = new BindyCsvDataFormat(Postback.class);
        BindyCsvDataFormat preferenceCustomer = new BindyCsvDataFormat(Preference.class);

        FileInformation fileInformation = new FileInformation();

        this.getContext().getShutdownStrategy().setTimeout(3);

        ThreadPoolProfile customThreadPool = new ThreadPoolProfileSupport("drcc");
        customThreadPool.setMaxPoolSize(200);
        this.getContext().getExecutorServiceStrategy().registerThreadPoolProfile(customThreadPool);
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        String ftpInPreferences = p.getProperty("ftp.incoming.preferences.url");

        if (StringUtils.isNotBlank(ftpInPreferences) && enablePreferenceParsing) {

            String ftpInUser = p.getProperty("ftp.incoming.preferences.user");
            String ftpInPassword = p.getProperty("ftp.incoming.preferences.password");
            String ftpInUrl = String.format("%s?username=%s&password=%s&download=true&delete=false&recursive=false&scheduler=quartz2&scheduler.cron=0+2+*+*+*+?&" +
                    "idempotent=true&idempotentRepository=#fileStorePreferences", ftpInPreferences, ftpInUser, ftpInPassword);

            from(ftpInUrl).id("preferencesFTPDownload")
                    .log("downloading ${file:name}")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .setHeader("fileType", simple("Notification Preferences"))
                    .to("bean:beanService?method=createFileLog")
                    .to("file:work/drcc/preferences/zipped/?fileName=${file:onlyname}");

            from("file:work/drcc/preferences/zipped/").id("preferenceDeltaUnzipper").unmarshal().gzip()
                    .to("file:work/drcc/preferences/")
                    .log("cdw unzipped");

            from("file:work/drcc/preferences/").id("preferenceRoute")
                    .onCompletion().log("Preference data  processing finished").to("direct:finishPreferences").end()
                    .log("Processing preference data ${file:name}")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .to("sql:SET unique_checks=0;")
                    .to("sql:SET foreign_key_checks=0;")
                    .split(body().tokenize("\n")).streaming()
                    .setBody(simple("body.replaceAll(\"\\t\",\"¶\")"))
//			.log("${body}")
                    .choice()
                    .when(simple("${body} contains 'SA_ID'"))
                    .log("Skipping first line")
                    .endChoice()
                    .otherwise()
                    .to("seda:processPreference?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .endChoice();


            from("direct:finishPreferences")
                    .delay(4000)
                    .to("sql:SET foreign_key_checks=1;")
                    .to("sql:SET unique_checks=1")
                    .to("bean:beanService?method=updateFileLogFinished")
                    .to("jpa:com.inenergis.entity.log.FileProcessorLog")
                    .log("Delayed finishing of preference import");

            from("seda:processPreference?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .unmarshal(preferenceCustomer)
                    .split(body())
                    .process(new Pojo2Map()) //converts the map with String,Participant into a map for each field
                    .process(new BodyInspector("prefs"))
//			.log("NOT_OPT_ID   ${body[NOTIFICATION_OPTION_ID]}, SA_ID  ${body[SA_ID]}  PERSON_ID  ${body[PERSON_ID]} NOT_PROG  ${body[NOTIFICATION_PROGRAM]}")
                    .to(PREFERENCES_INSERT_OR_UPDATE_SQL);
        }

        String ftpIn = p.getProperty("drcc.ftp.in.url");
        String ftpInUser = p.getProperty("drcc.ftp.in.user");
        String ftpInPassword = p.getProperty("drcc.ftp.in.password");
        String ftpInUrl = String.format("%s?username=%s&password=%s&download=true&delete=false&recursive=true&scheduler=quartz2&scheduler.cron=0+0/10+7-22+?+*+MON-SUN+*&sorter=#cvsFileSorter&idempotent=true&idempotentRepository=#fileStoreEvents", ftpIn, ftpInUser, ftpInPassword);

        if (StringUtils.isBlank(ftpIn)) {
            useIncomingFtp = false;
        }

        /**
         * TODO, configure this via profiles
         * for testing you can configure the scheduler differently, e.g. every minute 0/1+... or between 1 and 23 hours ...+1-23+...
         * Replace localhost with the actual server
         */
        if (StringUtils.isNotBlank(ftpIn) && useIncomingFtp) {
            from(ftpInUrl)
                    .log("downloading ${file:name}")
                    .to("file:work/drcc/incoming/?fileName=${file:onlyname}"); //use file:onlyname as otherwise the relative path will be used as well
        }

		
		/*
         * Camel does not support wild cards in the doneFileName it seems apart form the e.g. ${file:name}.done
		 */
        from("file:work/drcc/incoming?include=.*_create_trigger_[0-9]{8}.csv")
                .id("triggerPoll")
                .log("Trigger file found")
                .bean(fileInformation, "readTriggerFile");


        from("file:work/drcc/incoming?include=([a-zA-Z]{2,3})_(create|cancel)_([a-zA-Z_]*)_([0-9]{8}).csv&sorter=#cvsFileSorter&doneFileName=${file:name}.trigger").id("csvFileReader")
                .to("direct:wiretap")
                .log("************************************ Processing ${file:name} **************************************")
                .choice()
                .when(simple("${file:name} contains 'details'")).to("direct:pdp_sr_details").endChoice()
                .when(simple("${file:name} contains 'exceptions'")).to("direct:pdp_sr_exceptions").endChoice()
                .when(simple("${file:name} contains 'notifications'")).to("direct:notifications").endChoice()
                .when(simple("${file:name} contains 'participants'")).to("direct:pdp_sr_participants").endChoice()
                .otherwise().log("Ignoring ${file:name}").to("mock:result");

        from("direct:wiretap").id("wiretapRoute")
                .log("Archiving ${file:name}")
                .to("file:work/drcc/archive");

        from("direct:wiretapPostback").id("wiretapRoutePostback")
                .log("Archiving Postback ${file:name}")
                .to("file:work/drcc/archive/postback");

        /**
         * Route for processing the details file
         */
        from("direct:pdp_sr_details")
                .errorHandler(deadLetterChannel("direct:deadLetterChannel").useOriginalMessage().maximumRedeliveries(2)).id("pdpSrDetailsRoute")
                .log("Processing details message ${file:name}")
                .wireTap("direct:route_2_output")
                .split(body().tokenize("\n")).streaming()
                .log("Before setting header")
                .setHeader("CSV_LINE", simple("${body}"))
                .unmarshal(bindyDetail)
                .split(body())
                .bean(eventService, "processNewEvent")
                .log("BEfore pojo2Map")
                .process(new Pojo2Map())
                .log("Insert")
                .to("sql:INSERT INTO PDP_SR_EVENT(`EVENT_UNIQUE_ID`,`EVENT_PROGRAM`,`EVENT_NAME`,`EVENT_OPTIONS`,`EVENT_START`,`EVENT_END`,`EVENT_TYPE`,`EVENT_STATE`,`FILES_RECEIVED`) VALUES(:#uniqueId,:#eventProgram,:#eventName,:#eventOptions,:#eventStartDateTime,:#eventEndDateTime,:#eventType,:#eventState,NOW())")
                .log("done");


        /**
         * Route for processing of the participants csv file. Run after the details file
         */

        from("direct:pdp_sr_participants").id("pdpSrNotificationParticipants")
                .onCompletion().log("Participant  processing finished").end()
                .log("Processing participant message ${file:name}")
                .wireTap("direct:route_2_output")
                .split(body().tokenize("\n")).streaming().parallelProcessing() //.executorServiceRef("drcc") //.executorService(threadPool)
                .unmarshal(bindyParticipant)
                .split(body())
                .bean(eventService, "retrieveEventId")
                .process(new Pojo2Map()) //converts the map with String,Participant into a map for each field
                .to("sql:INSERT INTO PDP_SR_PARTICIPANTS(`ACCT_ID`,`DRUID`,`PREMISE_ID`,`SA_ID`,`SERVICE_ADDRESS`,`SERVICE_POINT_ID`,`PDP_SR_EVENT_EVENT_ID`) VALUES(:#acctId,:#myEnergyId,:#premiseID,:#saId,:#serviceAddress,:#servicePointId,:#eventId)");

        from("direct:route_2_output").id("route2Output")
                .log("Routing ${file:name} to vendor")
                .to("file:work/drcc/output/events/?fileName=${file:onlyname}");


        from("direct:pdp_sr_exceptions").id("pdpExceptionsRoute")
                .log("Processing exceptions message ${file:name}")
                .to("file:work/drcc/output/exceptions");

        from("direct:notifications").id("Notifications")
                .bean(eventService, "setEventIdOnHeader")
                .onCompletion().onCompleteOnly().log("******************************* ${file:name} processing finished ${headers} **************************").to("seda:vendor_check?size=40&blockWhenFull=true").end()
                .log("Processing notification message ${file:name}")
                .split(body().tokenize("\n")).streaming().parallelProcessing() //.executorService(threadPool)
                .setHeader("CSV_LINE", simple("${body}"))
                .unmarshal(bindyNotification)
                .split(body())
                .bean(eventService, "retrieveParticipantId") //finds the participant ID and sets it on the bean
                .process(new NotificationProcessor()) //converts one Notification into an array of values for the SQL insert
                .to("sql:INSERT INTO PDP_SR_NOTIFICATIONS(`PREFERENCE_CATEGORY`,`REC_ID`,`MESSAGE_TEMPLATE`,`PERSON_ID`,`NOTIFY_BY`,`NOTIFY_BY_VALUE`,`LANGUAGE`,`CREATION_TIMESTAMP`,`EVENT_DISPLAY_DAYNAME`,`EVENT_DISPLAY_EVENT_DATE`,`EVENT_CANCEL_STATUS`,`EVENT_DISPLAY_PREMISE_ADDR`,`SECOND_EVENT_DISPLAY_PREMISE_ADDR`,`NO_ADDITIONAL_ADDR_PREMISES`,`VENDOR_STATUS`,`PDP_RESERVATION_CAPACITY`,`PDP_SR_EVENT_EVENT_ID`,`PDP_SR_PARTICIPANTS_PARTICIPANT_ID`,`PDP_SR_VENDOR_VENDOR_ID`,`CSV_LINE`,`VENDOR_STATUS_TIMESTAMP`) VALUES(#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#)");

        from("direct:route_notification").id("routeNotification")
                .to("direct:vendor_out_notification");

        from("direct:vendor_out_notification").id("sendToVendor")
                .marshal(bindyNotification)
                .process(new BodyInspector("Vendor1"))
                .aggregate(constant("${header.SFTP_DIRECTORY}/${file:name}"), new StringBodyAggregationStrategy()).completionSize(1000).completionTimeout(1000)
                .convertBodyTo(String.class)
                .to("file:work/drcc/output/?fileName=${header.SFTP_DIRECTORY}/${file:name}&fileExist=Append");

        from("seda:vendor_check?concurrentConsumers=4&size=40&blockWhenFull=true").id("vendorCheck")
                .log("Vendor check on ${file:name}")
                .choice()
                .when(simple("${file:name} contains 'pdp'")).setHeader("PREF_CATEGORY", constant("PDP")).to("seda:check_duplicates?size=40&blockWhenFull=true").endChoice()
                .when(simple("${file:name} contains 'sr'")).setHeader("PREF_CATEGORY", constant("SMARTRATE")).to("seda:check_duplicates?size=40&blockWhenFull=true").endChoice()
                .otherwise().log("Ignoring ${file:name}").end();//do some error handling here

        from("seda:check_duplicates?concurrentConsumers=4&size=40&blockWhenFull=true").id("checkDuplicates")
                .onCompletion().to("sql:update PDP_SR_EVENT set DEDUP_COMPLETE = NOW() where EVENT_ID = :#EVENT_ID").end()
                .log("Checking for duplicates on ${header.NOTIFY_BY} for event ${header.EVENT_ID} #{header.SFTP_DIRECTORY}")
                .to("sql:select NOTIFICATION_ID, NOTIFY_BY, NOTIFY_BY_VALUE, LANGUAGE, :#EVENT_ID as EVENT_ID,PDP_SR_PARTICIPANTS_PARTICIPANT_ID,EVENT_DISPLAY_PREMISE_ADDR, CSV_LINE, count(*) as c from PDP_SR_NOTIFICATIONS where PDP_SR_EVENT_EVENT_ID = :#EVENT_ID and NOTIFY_BY = :#NOTIFY_BY group by NOTIFY_BY_VALUE, LANGUAGE") //having c>1
                .log("After sql query ${header.NOTIFY_BY} size ${header.CamelSqlRowCount}")
                .split(body()).streaming().parallelProcessing() //.executorService(threadPool)
                .setHeader("DUP_COUNT", simple("${body[c]}"))
                .process(new BodyInspector("DUP_1"))
                .enrich("sql:select EVENT_DISPLAY_PREMISE_ADDR from PDP_SR_NOTIFICATIONS where NOTIFICATION_ID <> :#NOTIFICATION_ID and NOTIFY_BY = :#NOTIFY_BY and NOTIFY_BY_VALUE = :#NOTIFY_BY_VALUE and PDP_SR_EVENT_EVENT_ID= :#EVENT_ID and LANGUAGE = :#LANGUAGE and EVENT_DISPLAY_PREMISE_ADDR <> :#EVENT_DISPLAY_PREMISE_ADDR", new MapMergeAggregationStrategy(ADDRESS_DETAILS))
                .bean(eventService, "mergeAddress")
                .choice()
                .when(simple("${header.DUP_COUNT} == 1"))
                .setHeader("NOTIFICATION_ID", simple("${body[NOTIFICATION_ID]}"))
                //jusr for testing .to("sql:update PDP_SR_NOTIFICATIONS set VENDOR_STATUS = 'ORIGINAL' where NOTIFICATION_ID = :#NOTIFICATION_ID")
                .setBody(simple("${body[CSV_LINE]}"))
                .to("seda:write_original?size=40&blockWhenFull=true")
                .endChoice()
                .otherwise()
                .to("seda:update_duplicates?size=40&blockWhenFull=true");

        from("seda:update_duplicates?concurrentConsumers=40&size=40&blockWhenFull=true").id("updateDuplicates")
                .to("sql:update PDP_SR_NOTIFICATIONS set NO_ADDITIONAL_ADDR_PREMISES = :#ADDRESS_SIZE, SECOND_EVENT_DISPLAY_PREMISE_ADDR = :#SECOND_ADDRESS where NOTIFICATION_ID = :#NOTIFICATION_ID")
                .to("sql:update PDP_SR_NOTIFICATIONS set VENDOR_STATUS = 'DUPLICATE', DISPLAY_MESSAGE = 'DUPLICATE', DUPLICATE_OF = :#NOTIFICATION_ID where PDP_SR_EVENT_EVENT_ID = :#EVENT_ID and NOTIFY_BY = :#NOTIFY_BY and NOTIFY_BY_VALUE = :#NOTIFY_BY_VALUE and NOTIFICATION_ID <> :#NOTIFICATION_ID AND LANGUAGE = :#LANGUAGE;")
                .setHeader("NOTIFICATION_ID", simple("${body[NOTIFICATION_ID]}"))
                .setBody(simple("${body[CSV_LINE]}"))
                .to("seda:write_original?size=40&blockWhenFull=true");

        from("seda:write_original?concurrentConsumers=40&size=40&blockWhenFull=true").id("writeOriginal")
                .unmarshal(bindyNotification)
                .split(body())
                .bean(eventService, "updateCSV")
                .split(body())
                .marshal(bindyNotification)
                //do we need to do some sorting? Because we process lines in parallel they might end up in random order
                .aggregate(header(Exchange.FILE_NAME_ONLY), new StringBodyAggregationStrategy()).completionTimeout(8000)
                .log("Write to file ${file:name}")
                .convertBodyTo(String.class)
                .log("Line count ${header.LINE_COUNT}")
                .to("file:work/drcc/output/?fileName=${header.NOTIFT_BY}/${file:onlyname}&fileExist=Append&doneFileName=${file:name}.done")
                .to("seda:upload_notification?size=40&blockWhenFull=true");

        if (useIncomingFtp) {
            from("seda:upload_notification?size=40&blockWhenFull=true")
                    .bean(eventService, "compileDestinationURLs")
                    .log("uploading file to sftp: ${file:onlyname}")
                    .recipientList(simple("${header.FTP_ENDPOINTS}"))
                    .to("sql:update PDP_SR_EVENT set VENDOR_FILES_SENT = NOW() where EVENT_ID = :#EVENT_ID");
        } else {
            from("seda:upload_notification?size=40&blockWhenFull=true")
                    .bean(eventService, "compileDestinationURLs")
                    .to("sql:update PDP_SR_EVENT set VENDOR_FILES_SENT = NOW() where EVENT_ID = :#EVENT_ID")
                    .log("dummy upload to Endpoints ${header.FTP_ENDPOINTS}");
        }

        /**
         * For testing you can configure the scheduler differently, e.g. every minute 0/1+... or between 1 and 23 hours ...+1-23+...
         */
        for (Map.Entry<String, Profile.Section> entry : drccIni.entrySet()) {
            Profile.Section section = entry.getValue();
            if (section.get("protocol").equals("ftp") && useIncomingPostbackFtp) {
                String url = String.format(
                        "%s?username=%s&password=%s&download=%s&delete=%s&recursive=%s&scheduler=quartz2&scheduler.cron=%s",
                        section.get("url"), section.get("user"), section.get("password"),
                        section.get("download"), section.get("delete"), section.get("recursive"), section.get("cron"));

                from(url)
                        .log(String.format("downloading %s postback ${file:name}", section.getName()))
                        .to("bean:postbackPostFilter?method=" + section.get("postbackPostFilterMethod"))
                        .to("file:work/drcc/postback/?fileName=${file:onlyname}");
            }
        }

        /**
         * Route for creating random postback messages
         */
        from("file:work/drcc/postbackCreate?include=.*.csv")
                .onCompletion().log("Postback file ${file:name} written").end()
                .log("Creating random postback messages for ${file:name}")
                .split(body().tokenize("\n")).streaming()
                .parallelProcessing()
                .unmarshal(bindyNotification)
                .split(body())
                .bean(eventService, "createPostback")
                .choice().when(body().isNotNull()).to("direct:write_postback").endChoice();

        from("direct:write_postback")
                .marshal(bindyPostback)
                .aggregate(simple("${file:name}"), new StringBodyAggregationStrategy()).completionTimeout(8000)
                .to("file:work/drcc/outputPostback/?fileName=postback_${file:onlyname}&fileExist=Append&doneFileName=${file:name}.done");


        /**
         * Route for processing of the Postback files
         */
        from("file:work/drcc/postback?include=.*.csv")
                .errorHandler(defaultErrorHandler().redeliveryDelay(2000).maximumRedeliveries(10).backOffMultiplier(4).retryAttemptedLogLevel(LoggingLevel.WARN).logStackTrace(true))
                .onCompletion().log("Post back file ${file:name} processed").end()
                .id("postbackFileReader")
                .to("direct:wiretap")
                .log("Processing ${file:name}")
                .bean(eventService, "loadVendorMapping")
                .split(body().tokenize("\n")).streaming()
                .parallelProcessing()
                .setHeader("CSV_LINE", simple("${body}"))
                .unmarshal(bindyPostback)
                .split(body())
                .process(new Pojo2Map()) //this converts the pojo into a Map which we can use in the sql query
                .setHeader("CONTACT_STATUS", simple("${body[contactStatus]}"))
                .setHeader("CONTACT_TIMESTAMP", simple("${body[contactTimestamp]}"))
                .setHeader("NOTIFICATION_ID", simple("${body[drccNotifRec]}"))
                .setHeader("NOTIFY_BY", simple("${body[notifyBy]}"))
                .setHeader("NOTIFY_BY_VALUE", simple("${body[notifyByValue]}"))
                .to("sql:set SESSION innodb_lock_wait_timeout=120")
                .enrich("sql:select PDP_SR_PARTICIPANTS_PARTICIPANT_ID,PDP_SR_VENDOR_VENDOR_ID, PDP_SR_EVENT_EVENT_ID from PDP_SR_NOTIFICATIONS where NOTIFICATION_ID = :#NOTIFICATION_ID;") //timestamp was just updated
                .split(body())
                .setHeader("PARTICIPANT_ID", simple("${body[PDP_SR_PARTICIPANTS_PARTICIPANT_ID]}"))
                .setHeader("PDP_SR_VENDOR_VENDOR_ID", simple("${body[PDP_SR_VENDOR_VENDOR_ID]}"))
                .setHeader("EVENT_ID", simple("${body[PDP_SR_EVENT_EVENT_ID]}"))
                .to("sql:update PDP_SR_EVENT set FIRST_POSTBACK_RECEIVED = NOW() where EVENT_ID = :#EVENT_ID and FIRST_POSTBACK_RECEIVED is null")
                .to("sql:update PDP_SR_EVENT set LAST_POSTBACK_RECEIVED = NOW() where EVENT_ID = :#EVENT_ID")
                .bean(eventService, "checkVendorMapping")
                .to("sql:update PDP_SR_NOTIFICATIONS set VENDOR_STATUS = :#CONTACT_STATUS, VENDOR_STATUS_TIMESTAMP = :#CONTACT_TIMESTAMP, DISPLAY_MESSAGE = :#DISPLAY_MESSAGE, SUCCESSFUL_NOTIFICATION = :#SUCCESSFUL_NOTIFICATION where NOTIFICATION_ID = :#NOTIFICATION_ID and VENDOR_STATUS_TIMESTAMP <= :#CONTACT_TIMESTAMP")
                .to("sql:update PDP_SR_NOTIFICATIONS set VENDOR_STATUS_TIMESTAMP = :#CONTACT_TIMESTAMP, SUCCESSFUL_NOTIFICATION = :#SUCCESSFUL_NOTIFICATION where DUPLICATE_OF = :#NOTIFICATION_ID and VENDOR_STATUS_TIMESTAMP <= :#CONTACT_TIMESTAMP")
                .enrich("sql:select PDP_SR_PARTICIPANTS_PARTICIPANT_ID from PDP_SR_NOTIFICATIONS where (NOTIFICATION_ID = :#NOTIFICATION_ID or DUPLICATE_OF = :#NOTIFICATION_ID) and VENDOR_STATUS_TIMESTAMP <= :#CONTACT_TIMESTAMP;")
                .bean(eventService, "createInList") //Aurora had problems with the update with join for whatever reason. Doing extra query to retrieve the IDs and then iterating over the ids to do the UPDATE
                .split(body())
                .setHeader("PARTICIPANT_IDS", simple("${body}"))
                .choice()
                .when(simple("${header.SUCCESSFUL_NOTIFICATION} != 'DELIVERED'"))
                .to("direct:process_unsuccess_postback")
                .endChoice()
                .otherwise()
                .to("sql:update PDP_SR_PARTICIPANTS set PDP_SR_PARTICIPANTS.SUCCESSFUL_NOTIFICATION = :#SUCCESSFUL_NOTIFICATION where PARTICIPANT_ID = :#PARTICIPANT_IDS and PDP_SR_PARTICIPANTS.SUCCESSFUL_NOTIFICATION != 'DELIVERED';");


        from("direct:process_unsuccess_postback")
                .process(new BodyInspector("UnsuccessPostback"))
                .enrich("sql:select count(*) AS SUCCESS_COUNT from PDP_SR_NOTIFICATIONS where PDP_SR_PARTICIPANTS_PARTICIPANT_ID = :#PARTICIPANT_IDS and (NOTIFY_BY_VALUE != :#NOTIFY_BY_VALUE OR NOTIFY_BY != :#NOTIFY_BY) and SUCCESSFUL_NOTIFICATION = 'DELIVERED';")
                .split(body())
                .choice()
                .when(simple("${body[SUCCESS_COUNT]} == 0"))
                .to("sql:update PDP_SR_PARTICIPANTS set PDP_SR_PARTICIPANTS.SUCCESSFUL_NOTIFICATION = :#SUCCESSFUL_NOTIFICATION where PARTICIPANT_ID = :#PARTICIPANT_IDS ;")
                .endChoice()
                .otherwise().log("Not updating because success count > 0 on participant ${header.PARTICIPANT_IDS} notification ${header.NOTIFICATION_ID}");


        /**
         * Dead letter channel
         */

        from("direct:deadLetterChannel")
                .log("DLC ${file:name}")
                .setBody(simple("${header.CSV_LINE}"))
                .process(new BodyInspector("DLC"))
                .to("file:work/drcc/dlc/?fileName=${date:now:yyyyMMdd}/${file:name}&fileExist=Append");

    }
}