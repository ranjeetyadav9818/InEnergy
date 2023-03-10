package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.beans.CustomerDataPostProcessor;
import com.inenergis.microbot.camel.csv.NewCustomer;
import com.inenergis.microbot.camel.processors.AssignCustomerToHeaderProcessor;
import com.inenergis.microbot.camel.processors.ErrorFileCreatorProcessor;
import com.inenergis.microbot.camel.processors.OriginalFileNameSetterProcessor;
import com.inenergis.microbot.camel.processors.PrepareHeaderProcessProcessor;
import com.inenergis.microbot.camel.services.ConfigurationService;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class GenericCDWRouteBuilder extends RouteBuilder {

    public static final String ORIGINAL_MESSAGE = "originalMessage";

    private boolean enableCustomerRecordParsing = true;

    public CustomerDataPostProcessor customerPostProcessor = new CustomerDataPostProcessor();
    private Processor errorFileCreator = new ErrorFileCreatorProcessor();
    private Processor originalFileNameSetter = new OriginalFileNameSetterProcessor();
    private Processor prepareHeaderProcess = new PrepareHeaderProcessProcessor();
    private Processor assignCustomerToHeader = new AssignCustomerToHeaderProcessor();

    protected Profile.Section config;
    protected BindyCsvDataFormat bindyCustomer;
    protected String routeName;

    @Autowired
    private Wini drccIni;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void configure() throws Exception {
        Properties p = configurationService.appProperties();

        if (!ConstantsProviderModel.TRUE.equals(p.getProperty("route.enableNewCustomerCdw"))) {
            log.info(" GenericCDWRouteBuilder disabled ");
            return;
        } else {
            log.info(" GenericCDWRouteBuilder enabled ");
        }

        init(NewCustomer.class, "cdw");
        configureDeadLetter();
        configureDeltaFile(bindyCustomer);
    }

    protected void init(Class<?> csvParserClass, String routeName) throws IOException {
        bindyCustomer = new BindyCsvDataFormat(csvParserClass);
        config = drccIni.get(routeName);
        this.routeName = routeName;
    }

    protected void configureDeadLetter() {
        from(config.get("seda_failedCustomerRow")).id(config.get("idDeadletter"))
                .log("error with ${body}")
                .setHeader("exception", simple("${exception}"))
                .to("bean:beanService?method=saveInHeaderFileLog")
                .process(errorFileCreator)
                .to("jpa:com.inenergis.entity.log.FileProcessorError");
    }

    protected void configureDeltaFile(BindyCsvDataFormat bindyCustomer) throws IOException {
        String ftpIn = config.get("drcc.ftp.incoming.cdw.url");
        String ftpInUser = config.get("drcc.ftp.incoming.cdw.user");
        String ftpInPassword = config.get("drcc.ftp.incoming.cdw.password");
        final String deleteFtp = config.get("deleteFtp");

        if (StringUtils.isNotEmpty(ftpIn) && enableCustomerRecordParsing) {

            from(String.format("%s?username=%s&password=%s&download=true&delete=%s&recursive=false&scheduler=quartz2&scheduler.cron=0+0+*+*+*+?&" +
                    "idempotent=true&idempotentRepository=#fileStoreNewCDW", ftpIn, ftpInUser, ftpInPassword, deleteFtp)).id(config.get("idFTPDownload"))
                    .log("downloading ${file:name}")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .to("bean:beanService?method=createFileLog")
                    .to("file:" + config.get("customerRecordFolder") + "/?fileName=${file:onlyname}");

            from("file:" + config.get("customerRecordFolder")).id(config.get("idDeltaUnzipper")).unmarshal().gzip()
                    .to("file:" + config.get("customerRecordUnzipFolder") + "?doneFileName=${file:name}.done&fileName=${file:onlyname.noext}.unzipped")
                    .log("cdw unzipped");

            final String charset = config.get("charset");
            final String charsetParam = (StringUtils.isNotEmpty(charset) ? String.format("charset=%s&", charset) : StringUtils.EMPTY);
            from("file:" + config.get("customerRecordUnzipFolder") + "?" + charsetParam + String.format("delete=%s", config.get("delete"))).id(config.get("idCustomerDeltaDataRoute"))
                    .onCompletion()
//                   //todo place here .to(unenroll queue)
                    .log("Customer data  processing finished")
                    .to("bean:beanService?method=updateFileLogFinished")
                    .to("jpa:com.inenergis.entity.log.FileProcessorLog")
                    .end()
                    .log("Processing customer data ${file:name}")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .process(prepareHeaderProcess)
                    .process(originalFileNameSetter)
                    .split(body().tokenize("\n")).streaming()
                    .choice()
                    .when(simple("${body} contains 'SA STATUS'"))
                    .log("Skipping first line")
                    .endChoice()
                    .otherwise()
                    .to(config.get("nodeProcessDeltaCustomer"))
                    .endChoice();

            from(config.get("nodeProcessDeltaCustomer")).id(config.get("idVModelDeltaSaving"))
                    .onException(Exception.class).to(config.get("seda_failedCustomerRow")).handled(true).end()
                    .setHeader(ORIGINAL_MESSAGE).simple("${body}")
                    .setBody(simple("body.replaceAll(\"\\t\",\"¶\")"))
                    .unmarshal(bindyCustomer)
                    .split(body())
                    .process(assignCustomerToHeader)
                    .choice()
                    .when(simple("${body.getPERSON_ID} == null"))
                    .endChoice()
                    .otherwise()
                    .bean(customerPostProcessor, "postProcess")
                    .to("bean:beanService?method=saveVModel")
                    .endChoice();

            //todo place here from(unenroll queue)... for unenrollments due to delta file change
        }
    }
}