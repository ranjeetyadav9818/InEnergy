package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.beans.CustomerDataPostProcessor;
import com.inenergis.microbot.camel.beans.EventService;
import com.inenergis.microbot.camel.csv.Customer;
import com.inenergis.microbot.camel.processors.AssignCustomerToHeaderProcessor;
import com.inenergis.microbot.camel.processors.CustomerProcessor;
import com.inenergis.microbot.camel.processors.ErrorFileCreatorProcessor;
import com.inenergis.microbot.camel.processors.OriginalFileNameSetterProcessor;
import com.inenergis.microbot.camel.processors.Pojo2Map;
import com.inenergis.microbot.camel.processors.PrepareHeaderProcessProcessor;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class CDWRouteBuilder extends RouteBuilder {

    public static final String CUSTOMER_DATA_INSERT_SQL = "sql:INSERT INTO CUSTOMER_DATA(`SA_STATUS`, `SERVICE_AGREEMENT_ID`, `ACCOUNT_ID`, `PREMISE_ID`, `METER_ID`, `PERSON_ID`, `CUSTOMER_NAME`, `SERVICE_ADDRESS1`, `SERVICE_ADDRESS2`, "
            + " `SERVICE_CITY_UPR`, `SERVICE_STATE`, `SERVICE_POSTAL`, `PHONE`, `MAILING_ADDRESS1`, `MAILING_ADDRESS2`, `MAILING_CITY_UPR`, `MAILING_STATE`, `MAILING_POSTAL`, `RATE_SCHEDULE`, `RES_YN_IND`, `SUBSTATION`, `FEEDER`, "
            + " `SERVICE_POINT_ID`, `MEDICAL_BASELINE_IND`, `LIFE_SUPPORT_IND`, `SMART_RATE_IND`, `SA_SP_START_DTTM`, `SA_SP_STOP_DTTM`, `PREMISE_TYPE`, `SR_START_DATE`, `SR_END_DATE`, `PDP_ENROLLED`, `PDP_STATUS`, `PDP_START_DATE`,"
            + " `PDP_STOP_DATE`, `PDP_PLAN_OPTIONS`, `ELEC_USAGE_NONRES`, `PDP_RESV_CAP_VAL`, `HAS_3RD_PARTY_DRP`, `PHONE_EXTENSION`, `SUPPLIER_IS_DRP`, `UNIQ_SA_ID`, `UNIQ_SA_ID_CREATE_DATE`, `UNIQ_SA_ID_WARN_FLAG`, `SA_UUID`, `DO_BUS_AS_NM`, "
            + " `SA_START_DATE`, `SA_END_DATE`, `SA_NAICS`, `BILL_CYCLE_CD`, `CUST_CLASS_CD`, `REVENUE_CLASS_DESC`, `FERA_FLAG`, `BILL_SYSTEM`, `CUST_SIZE`, `MARKET_SEGMENT`, `CARE_FLAG`, `OPERATION_AREA`, `PREM_BASELINE_CHAR`, `MTR_BADGENBR`, "
            + " `SM_STATUS`, `MTR_INSTALL_DT`, `MTR_UNINSTALL_DT`, `SM_MODULE_MFR`, `MTR_CONFIG_TYPE`, `MTR_READ_FREQ`, `MTR_MFG`, `PDP_BILL_PROTECTION`, `SR_BILL_PROTECTION`, `SA_TYPE_CD`, `CUSTOMER_MDMA_COMPANY_NAME`, `CUSTOMER_MSP_COMPANY_NAME`, "
            + " `CUSTOMER_LSE_COMPANY_NAME`, `CUST_METER_READ_CYCLE12`, `CUST_SERVICE_VOLTAGE_CLASS`, `SA_SP_ID`, `RATE_CODE_EFFECTIVE_DATE`, `SERVICE_TYPE`, `BUSINESS_ACTIVITY_DESC`, `BUS_OWNER`, "
            + " `DIVISION_CODE_19`, `CIRCUIT_NUMBER`, `SUB_STATION_NUMBER`, `COUNTY`, `CLIMATE_ZONE`, `ESS_DIVISION_CODE`, `SOURCE_SIDE_DEVICE_NUMBER`, `LATITUDE`, `LONGITUDE`, `TRFMR_NUMBER`, `TRFMR_BDG_NUMBER`, `ROB_CODE`,"
            + " `CUSTOMER_MDMA_CODE`, `CUSTOMER_MSP_CODE`, `CUSTOMER_LSE_CODE`, `ESSENTIAL_CUSTOMER_FLAG`) "
            + "VALUES(#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#,#)";


    public static final String PERSON_INSERT_SQL = "sql:INSERT INTO PERSON(`PERSON_ID`,`CUSTOMER_NAME`,`BUSINESS_NAME`,`PHONE_EXTENSION`,`BUS_OWNER`) "
            + "VALUES(:#PERSON_ID,:#LAST_NAME,:#DO_BUS_AS_NM,:#PHONE_EXTENSION,:#BUS_OWNER) "
            + "ON DUPLICATE KEY UPDATE CUSTOMER_NAME=:#LAST_NAME, BUSINESS_NAME=:#DO_BUS_AS_NM, PHONE_EXTENSION=:#PHONE_EXTENSION, BUS_OWNER=:#BUS_OWNER";
    public static final String ACCOUNT_INSERT_SQL = "sql:INSERT INTO ACCOUNT(`ACCOUNT_ID`,`PERSON_ID`) VALUES(:#ACCOUNT_ID,:#PERSON_ID) ON DUPLICATE KEY UPDATE PERSON_ID = :#PERSON_ID";
    public static final String METER_INSERT_SQL = "sql:INSERT IGNORE INTO METER(`METER_ID`, `MTR_BADGENBR`, `SM_STATUS`, `MTR_INSTALL_DT`, `MTR_UNINSTALL_DT`, `SM_MODULE_MFR`, `MTR_CONFIG_TYPE`, `MTR_READ_FREQ`, `MTR_MFG`) "
            + "VALUES(:#METER_ID, :#MTR_BADGENBR, :#SM_STATUS, :#MTR_INSTALL_DT, :#MTR_UNINSTALL_DT, :#SM_MODULE_MFR, :#MTR_CONFIG_TYPE, :#MTR_READ_FREQ, :#MTR_MFG) "
            + "ON DUPLICATE KEY UPDATE MTR_BADGENBR=:#MTR_BADGENBR, SM_STATUS=:#SM_STATUS, MTR_INSTALL_DT=:#MTR_INSTALL_DT, MTR_UNINSTALL_DT=:#MTR_UNINSTALL_DT, SM_MODULE_MFR=:#SM_MODULE_MFR, MTR_CONFIG_TYPE=:#MTR_CONFIG_TYPE, "
            + " MTR_READ_FREQ=:#MTR_READ_FREQ, MTR_MFG=:#MTR_MFG";
    public static final String PREMISE_INSERT_SQL = "sql:INSERT INTO PREMISE(`PREMISE_ID`,`SERVICE_ADDRESS1`,`SERVICE_ADDRESS2`,`SERVICE_CITY_UPR`,`SERVICE_STATE`,`SERVICE_POSTAL`, `PREM_BASELINE_CHAR`, `COUNTY`, `PREMISE_TYPE`) "
            + "VALUES(:#PREMISE_ID,:#SERVICE_ADDRESS1,:#SERVICE_ADDRESS2,:#SERVICE_CITY_UPR,:#SERVICE_STATE,:#SERVICE_POSTAL, :#PREM_BASELINE_CHAR, :#COUNTY, :#PREMISE_TYPE) "
            + "ON DUPLICATE KEY UPDATE SERVICE_ADDRESS1=:#SERVICE_ADDRESS1,SERVICE_ADDRESS2=:#SERVICE_ADDRESS2,SERVICE_CITY_UPR=:#SERVICE_CITY_UPR,SERVICE_STATE=:#SERVICE_STATE,SERVICE_POSTAL=:#SERVICE_POSTAL, "
            + " PREM_BASELINE_CHAR=:#PREM_BASELINE_CHAR, COUNTY=:#COUNTY";
    public static final String AGREEMENT_POINT_MAP_INSERT_SQL = "sql:INSERT INTO AGREEMENT_POINT_MAP(`SERVICE_AGREEMENT_ID`,`SERVICE_POINT_ID`,`START_DATE`,`END_DATE`,`SA_SP_ID`) "
            + "VALUES(:#SA_ID,:#SP_ID,:#SA_SP_START_DTTM,:#SA_SP_STOP_DTTM, :#SA_SP_ID) ON DUPLICATE KEY UPDATE SERVICE_POINT_ID=:#SP_ID,START_DATE=:#SA_SP_START_DTTM,END_DATE=:#SA_SP_STOP_DTTM, SA_SP_ID=:#SA_SP_ID";

    public static final String SERVICE_POINT_INSERT_SQL = "sql:INSERT INTO SERVICE_POINT(`SERVICE_POINT_ID`,`PREMISE_ID`,`METER_ID`,`SUBSTATION`,`FEEDER`,`ELEC_USAGE_NONRES`, "
            + " `OPERATION_AREA`, `CUSTOMER_MDMA_COMPANY_NAME`, `CUSTOMER_MSP_COMPANY_NAME`, `CUST_METER_READ_CYCLE12`, `CUST_SERVICE_VOLTAGE_CLASS`, `SERVICE_TYPE`, `CIRCUIT_NUMBER`, `SUB_STATION_NUMBER`, "
            + " `SOURCE_SIDE_DEVICE_NUMBER`, `LATITUDE`, `LONGITUDE`, `TRFMR_NUMBER`, `TRFMR_BDG_NUMBER`, `ROB_CODE`,`CUSTOMER_MDMA_CODE`,`CUSTOMER_MSP_CODE`) "
            + "VALUES(:#SP_ID,:#PREMISE_ID,:#METER_ID,:#SUBSTATION,:#FEEDER,:#ELEC_USAGE_NONRES, :#OPERATION_AREA, :#CUSTOMER_MDMA_COMPANY_NAME, "
            + " :#CUSTOMER_MSP_COMPANY_NAME, :#CUST_METER_READ_CYCLE12, :#CUST_SERVICE_VOLTAGE_CLASS, :#SERVICE_TYPE, :#CIRCUIT_NUMBER, :#SUB_STATION_NUMBER, :#SOURCE_SIDE_DEVICE_NUMBER, :#LATITUDE, :#LONGITUDE, "
            + " :#TRFMR_NUMBER, :#TRFMR_BDG_NUMBER, :#ROB_CODE, :#CUSTOMER_MDMA_CODE, :#CUSTOMER_MSP_CODE) "
            + "ON DUPLICATE KEY UPDATE PREMISE_ID=:#PREMISE_ID,METER_ID=:#METER_ID,SUBSTATION=:#SUBSTATION,FEEDER=:#FEEDER,ELEC_USAGE_NONRES=:#ELEC_USAGE_NONRES, "
            + " OPERATION_AREA=:#OPERATION_AREA, CUSTOMER_MDMA_COMPANY_NAME=:#CUSTOMER_MDMA_COMPANY_NAME, CUSTOMER_MSP_COMPANY_NAME=:#CUSTOMER_MSP_COMPANY_NAME, CUST_METER_READ_CYCLE12=:#CUST_METER_READ_CYCLE12, "
            + " CUST_SERVICE_VOLTAGE_CLASS=:#CUST_SERVICE_VOLTAGE_CLASS, SERVICE_TYPE=:#SERVICE_TYPE, CIRCUIT_NUMBER=:#CIRCUIT_NUMBER, SUB_STATION_NUMBER=:#SUB_STATION_NUMBER, SOURCE_SIDE_DEVICE_NUMBER=:#SOURCE_SIDE_DEVICE_NUMBER, "
            + " LATITUDE=:#LATITUDE, LONGITUDE=:#LONGITUDE, TRFMR_NUMBER=:#TRFMR_NUMBER, TRFMR_BDG_NUMBER=:#TRFMR_BDG_NUMBER, ROB_CODE=:#ROB_CODE, CUSTOMER_MDMA_CODE=:#CUSTOMER_MDMA_CODE, CUSTOMER_MSP_CODE=:#CUSTOMER_MSP_CODE";

    public static final String SERVICE_AGREEMENT_INSERT_SQL = "sql:INSERT INTO SERVICE_AGREEMENT(`SERVICE_AGREEMENT_ID`,`SA_STATUS`,`MAILING_ADDRESS1`,`MAILING_ADDRESS2`,`MAILING_CITY_UPR`,`MAILING_STATE`,`MAILING_POSTAL`,`PHONE`, `RATE_SCHEDULE`, "
            + " `ACCOUNT_ID`,`MEDICAL_BASELINE_IND`,`LIFE_SUPPORT_IND`,`HAS_3RD_PARTY_DRP`, "
            + " `SUPPLIER_IS_DRP`, `UNIQ_SA_ID`, `UNIQ_SA_ID_CREATE_DATE`, `UNIQ_SA_ID_WARN_FLAG`, `SA_UUID`, `SA_START_DATE`, `SA_END_DATE`, `SA_NAICS`, `BILL_CYCLE_CD`, `CUST_CLASS_CD`, `REVENUE_CLASS_DESC`, `FERA_FLAG`, `BILL_SYSTEM`, `CUST_SIZE`, "
            + " `MARKET_SEGMENT`, `CARE_FLAG`, `SA_TYPE_CD`, `CUSTOMER_LSE_COMPANY_NAME`, `RATE_CODE_EFFECTIVE_DATE`, `BUSINESS_ACTIVITY_DESC`, `DIVISION_CODE_19`, `CLIMATE_ZONE`, `ESS_DIVISION_CODE`, `RES_IND`, "
            + " `CUSTOMER_LSE_CODE`, `ESSENTIAL_CUSTOMER_FLAG`) "
            + "VALUES(:#SA_ID,:#SA_STATUS,:#MAILING_ADDRESS1,:#MAILING_ADDRESS2,:#MAILING_CITY_UPR,:#MAILING_STATE,:#MAILING_ZIP,:#PHONE,:#RS_CD,:#ACCOUNT_ID,:#medicalBaseline,:#lifeSupport,"
            + " :#HAS_3RD_PARTY_DRP, :#supplierIsDRP, :#UNIQ_SA_ID, :#UNIQ_SA_ID_CREATE_DATE, :#UNIQ_SA_ID_WARN_FLAG, :#SA_UUID, :#SA_START_DATE, :#SA_END_DATE, :#SA_NAICS, "
            + " :#BILL_CYCLE_CD, :#CUST_CLASS_CD, :#REVENUE_CLASS_DESC, :#feraFlag, :#BILL_SYSTEM, :#CUST_SIZE, :#MARKET_SEGMENT, :#careFlag, :#SA_TYPE_CD, :#CUSTOMER_LSE_COMPANY_NAME, "
            + " :#RATE_CODE_EFFECTIVE_DATE, :#BUSINESS_ACTIVITY_DESC, :#DIVISION_CODE_19, :#CLIMATE_ZONE, :#ESS_DIVISION_CODE, :#resInd, :#CUSTOMER_LSE_CODE, :#ESSENTIAL_CUSTOMER_FLAG) "
            + "ON DUPLICATE KEY UPDATE SA_STATUS = :#SA_STATUS, MAILING_ADDRESS1=:#MAILING_ADDRESS1,MAILING_ADDRESS2=:#MAILING_ADDRESS2,MAILING_CITY_UPR=:#MAILING_CITY_UPR,MAILING_STATE=:#MAILING_STATE,MAILING_POSTAL=:#MAILING_ZIP,PHONE=:#PHONE, "
            + " RATE_SCHEDULE=:#RS_CD,ACCOUNT_ID=:#ACCOUNT_ID,MEDICAL_BASELINE_IND=:#medicalBaseline,LIFE_SUPPORT_IND=:#lifeSupport,HAS_3RD_PARTY_DRP=:#HAS_3RD_PARTY_DRP, SUPPLIER_IS_DRP=:#supplierIsDRP, "
            + " UNIQ_SA_ID=:#UNIQ_SA_ID, UNIQ_SA_ID_CREATE_DATE=:#UNIQ_SA_ID_CREATE_DATE, UNIQ_SA_ID_WARN_FLAG=:#UNIQ_SA_ID_WARN_FLAG, SA_UUID=:#SA_UUID, SA_START_DATE=:#SA_START_DATE, SA_END_DATE=:#SA_END_DATE, SA_NAICS=:#SA_NAICS, "
            + " BILL_CYCLE_CD=:#BILL_CYCLE_CD, CUST_CLASS_CD=:#CUST_CLASS_CD, REVENUE_CLASS_DESC=:#REVENUE_CLASS_DESC, FERA_FLAG=:#feraFlag, BILL_SYSTEM=:#BILL_SYSTEM, CUST_SIZE=:#CUST_SIZE, MARKET_SEGMENT=:#MARKET_SEGMENT, CARE_FLAG=:#careFlag, "
            + " SA_TYPE_CD=:#SA_TYPE_CD, CUSTOMER_LSE_COMPANY_NAME=:#CUSTOMER_LSE_COMPANY_NAME, RATE_CODE_EFFECTIVE_DATE=:#RATE_CODE_EFFECTIVE_DATE, "
            + " BUSINESS_ACTIVITY_DESC=:#BUSINESS_ACTIVITY_DESC, DIVISION_CODE_19=:#DIVISION_CODE_19, CLIMATE_ZONE=:#CLIMATE_ZONE, ESS_DIVISION_CODE=:#ESS_DIVISION_CODE, RES_IND=:#resInd, CUSTOMER_LSE_CODE=:#CUSTOMER_LSE_CODE, "
            + " ESSENTIAL_CUSTOMER_FLAG=:#ESSENTIAL_CUSTOMER_FLAG";

    public static final String PROGRAM_SA_ENROLLMENT_PDP_SQL = "sql:INSERT INTO PROGRAM_SA_ENROLLMENT(`UUID`,`PROGRAM_ID`,`AGGREGATOR_ID`,`SA_ID`,`ENROLLMENT_SOURCE`,`ENROLLMENT_CHANNEL`,`ENROLLMENT_STATUS`,`EFFECTIVE_START_DATE`,`EFFECTIVE_END_DATE`,"
            + " `DRMS_ID`,`UNENROLL_REASON`,`FSL`,`THIRD_PARTY_NAME`,`ORIGINAL_EFFECTIVE_START_DATE`,`BILL_PROTECTION`,`RESERVATION_CAPACITY_APPLIED_VALUE`,`PLAN_OPTIONS`) VALUES (:#UUID,1,NULL,:#SA_ID,'CDW','DRCM - CDW Parser',:#PDP_STATUS,:#PDP_START_DATE,"
            + " :#PDP_STOP_DATE,NULL,NULL,NULL,NULL,:#PDP_START_DATE,"
            + " :#pdpBillProtection,:#PDP_RESV_CAP_VAL,:#PDP_PLAN_OPTIONS) ON DUPLICATE KEY UPDATE UUID=:#UUID, PROGRAM_ID=1,AGGREGATOR_ID= NULL,SA_ID=:#SA_ID,ENROLLMENT_SOURCE='CDW',ENROLLMENT_CHANNEL='DRCM - CDW Parser',ENROLLMENT_STATUS=:#PDP_STATUS,"
            + " EFFECTIVE_START_DATE=:#PDP_START_DATE,EFFECTIVE_END_DATE=:#PDP_STOP_DATE,DRMS_ID= NULL,UNENROLL_REASON= NULL,FSL= NULL,THIRD_PARTY_NAME= NULL,ORIGINAL_EFFECTIVE_START_DATE=:#PDP_START_DATE,BILL_PROTECTION=:#pdpBillProtection,"
            + " RESERVATION_CAPACITY_APPLIED_VALUE=:#PDP_RESV_CAP_VAL,PLAN_OPTIONS = :#PDP_PLAN_OPTIONS";


    public static final String PROGRAM_SA_ENROLLMENT_SR_SQL = "sql:INSERT INTO PROGRAM_SA_ENROLLMENT(`UUID`,`PROGRAM_ID`,`AGGREGATOR_ID`,`SA_ID`,`ENROLLMENT_SOURCE`,`ENROLLMENT_CHANNEL`,`ENROLLMENT_STATUS`,`EFFECTIVE_START_DATE`,`EFFECTIVE_END_DATE`,"
            + " `DRMS_ID`,`UNENROLL_REASON`,`FSL`,`THIRD_PARTY_NAME`,`ORIGINAL_EFFECTIVE_START_DATE`,`BILL_PROTECTION`,`RESERVATION_CAPACITY_APPLIED_VALUE`,`PLAN_OPTIONS`) "
            + " VALUES (:#UUID,2,NULL,:#SA_ID,'CDW','DRCM - CDW Parser',:#PDP_STATUS,:#SR_START_DATE,:#SR_END_DATE,NULL,NULL,NULL,NULL,:#SR_START_DATE,:#srBillProtection,NULL,NULL) "
            + " ON DUPLICATE KEY UPDATE UUID=:#UUID, PROGRAM_ID=2,AGGREGATOR_ID= NULL,SA_ID=:#SA_ID,ENROLLMENT_SOURCE='CDW',ENROLLMENT_CHANNEL='DRCM - CDW Parser',ENROLLMENT_STATUS=NULL,"
            + " EFFECTIVE_START_DATE=:#SR_START_DATE,EFFECTIVE_END_DATE=:#SR_END_DATE,DRMS_ID= NULL,UNENROLL_REASON= NULL,FSL= NULL,THIRD_PARTY_NAME= NULL,ORIGINAL_EFFECTIVE_START_DATE=:#SR_START_DATE,BILL_PROTECTION=:#srBillProtection,"
            + " RESERVATION_CAPACITY_APPLIED_VALUE=NULL,PLAN_OPTIONS = NULL";
    public static final String ORIGINAL_MESSAGE = "originalMessage";

    @Autowired
    @Qualifier("appProperties")
    private Properties p;

    @Autowired
    private EventService eventService;

    private boolean enableCustomerRecordParsing = true;

    public CustomerDataPostProcessor customerPostProcessor = new CustomerDataPostProcessor();

    private Processor errorFileCreator = new ErrorFileCreatorProcessor();

    private Processor originalFileNameSetter = new OriginalFileNameSetterProcessor();

    private Processor prepareHeaderProcess = new PrepareHeaderProcessProcessor();

    private Processor assignCustomerToHeader = new AssignCustomerToHeaderProcessor();

    @Override
    public void configure() throws Exception {

        if (!ConstantsProviderModel.TRUE.equals(p.getProperty("route.enablePGECdw"))) {
            log.info(" CDWRouteBuilder disabled ");
            return;
        } else {
            log.info(" CDWRouteBuilder enabled ");
        }

        BindyCsvDataFormat bindyCustomer = new BindyCsvDataFormat(Customer.class);

        from("seda:failedCustomerRow").id("cdwDeadletter")
                .log("error with ${body}")
                .setHeader("exception", simple("${exception}"))
                .to("bean:beanService?method=saveInHeaderFileLog")
                .process(errorFileCreator)
                .to("jpa:com.inenergis.entity.log.FileProcessorError");

        configureInitialFile(bindyCustomer);
        configureDeltaFile(bindyCustomer);
    }

    private void configureDeltaFile(BindyCsvDataFormat bindyCustomer) throws IOException {

        String ftpIn = p.getProperty("ftp.incoming.cdw.url");
        String ftpInUser = p.getProperty("ftp.incoming.cdw.user");
        String ftpInPassword = p.getProperty("ftp.incoming.cdw.password");
        String ftpInUrl = String.format("%s?username=%s&password=%s&download=true&delete=false&recursive=false&scheduler=quartz2&scheduler.cron=0+0+*+*+*+?&" +
                "idempotent=true&idempotentRepository=#fileStoreCDW", ftpIn, ftpInUser, ftpInPassword);

        if (StringUtils.isNotEmpty(ftpIn) && enableCustomerRecordParsing) {

            from(ftpInUrl).id("cdwFTPDownload")
                    .log("downloading ${file:name}")
                    .to("file:work/drcc/customer/delta/?fileName=${file:onlyname}");


            from("file:work/drcc/customer/delta/").id("cdwDeltaUnzipper").unmarshal().gzip()
                    .to("file:work/drcc/customer/delta/unzip?doneFileName=${file:name}.done&fileName=${file:onlyname.noext}.unzipped")
                    .log("cdw unzipped");

            from("file:work/drcc/customer/delta/unzip/?charset=cp1252&delete=true").id("customerDeltaDataRoute")
                    .onCompletion()
                        .to("direct:unenrollDueToCDWChange")
                        .log("Customer data  processing finished")
                        .to("bean:beanService?method=updateFileLogFinished")
                        .to("jpa:com.inenergis.entity.log.FileProcessorLog")
                    .end()
                    .log("Processing customer data ${file:name}")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .to("bean:beanService?method=createFileLog")
                    .process(prepareHeaderProcess)
                    .process(originalFileNameSetter)
                    .split(body().tokenize("\n")).streaming()
                    .choice()
                    .when(simple("${body} contains 'SA STATUS'"))
                        .log("Skipping first line")
                    .endChoice()
                    .otherwise()
                        .to("direct:processDeltaCustomer")
                    .endChoice();

            from("direct:processDeltaCustomer").id("cdwVModelDeltaSaving")
                    .onException(Exception.class)
                        .to("seda:failedCustomerRow")
                        .maximumRedeliveries(2)
                        .redeliveryDelay(15000)
                        .handled(true)
                    .end()
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


            from("direct:unenrollDueToCDWChange").id("unenrollDueToCDWChange")
                    .setBody(simple("${header.customersForUnenrolling}"))
                    .to("bean:beanService?method=checkEnrollmentsEligibility")
                    .split().body()
                    .to("bean:enrollmentService?method=triggerUnenrollmentWorkflow")
                    .to("jpa:com.inenergis.entity.program.ProgramServiceAgreementEnrollment");

        }
    }

    private void configureInitialFile(BindyCsvDataFormat bindyCustomer) {
        if (enableCustomerRecordParsing) {
            from("file:work/drcc/customer/?charset=cp1252&delete=true").id("splitter")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .to("bean:beanService?method=createFileLog")
                    .split().tokenize("\n", 10000).streaming()
                    .to("file:work/drcc/customer/split/?charset=cp1252&fileName=${file:onlyname.noext}-_-${date:now:yyyyMMddHHmmssSSS}");

            from("file:work/drcc/customer/split/?charset=cp1252&delete=true").id("customerDataRoute")
                    .onCompletion().log("Customer data  processing finished").to("direct:finishCustomer").end()
                    .log("Processing customer data ${file:name}")
                    .setHeader("fileName", simple("${file:onlyname.noext}"))
                    .process(originalFileNameSetter)
                    .to("sql:SET unique_checks=0;")
                    .to("sql:SET foreign_key_checks=0;")
                    .log("unique keys and foreign checks disabled")
                    .split(body().tokenize("\n")).streaming()
                    .choice()
                    .when(simple("${body} contains 'SA STATUS'"))
                    .log("Skipping first line")
                    .endChoice()
                    .otherwise()
//                    .to("seda:processCustomerRaw?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .to("seda:processCustomer?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .endChoice();

            from("seda:processCustomer?size=40&concurrentConsumers=20&blockWhenFull=true").id("cdwVModelSaving")
                    .onException(Exception.class)
                        .setHeader(ORIGINAL_MESSAGE, simple("${property.originalMessage}"))
                        .setHeader("fileName", simple("${property.fileName}"))
                        .to("seda:failedCustomerRow")
                    .end() //Maybe affect number of rows in log
                    .setProperty(ORIGINAL_MESSAGE, simple("${body}"))
                    .setProperty("fileName", simple("${header.fileName}"))
                    .setBody(simple("body.replaceAll(\"\\t\",\"¶\")"))
                    .unmarshal(bindyCustomer)
                    .split(body())
                    .choice()
                    .when(simple("${body.getPERSON_ID} == null"))
                    .endChoice()
                    .otherwise()
                    .bean(customerPostProcessor, "postProcess")
                    .process(new Pojo2Map()) //converts the map with String,Participant into a map for each field
                    .to(PERSON_INSERT_SQL)
                    .to(ACCOUNT_INSERT_SQL)
                    .to(SERVICE_AGREEMENT_INSERT_SQL)
                    .to(PREMISE_INSERT_SQL)
                    .to(METER_INSERT_SQL)
                    .to(SERVICE_POINT_INSERT_SQL)
                    .to(AGREEMENT_POINT_MAP_INSERT_SQL)
                    .to("seda:processEnrollment?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .end();

            from("seda:processEnrollment?size=40&concurrentConsumers=20&blockWhenFull=true").id("cdwEnrollmentSaving")
                    .onException(Exception.class).to("seda:failedCustomerRow").end()
                    .choice()
                    .when(simple("${body[pdpEnrolled]} || ${body[PDP_START_DATE]} != null ")).to(PROGRAM_SA_ENROLLMENT_PDP_SQL).endChoice()
                    .when(simple("${body[smartRate]}  || ${body[SR_START_DATE]} != null ")).to(PROGRAM_SA_ENROLLMENT_SR_SQL).endChoice()
                    .end();

            from("seda:processCustomerRaw?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .setBody(simple("body.replaceAll(\"\\t\",\"¶\")"))
                    .unmarshal(bindyCustomer)
                    .split(body())
                    .choice()
                    .when(simple("${body.getPERSON_ID} == null"))
                    .endChoice()
                    .otherwise()
                    .bean(eventService, "checkCustomerCount")
                    .bean(customerPostProcessor, "postProcess")
                    .process(new CustomerProcessor()).id("CustomProcessor") //converts one Notification into an array of values for the SQL insert
                    .to(CUSTOMER_DATA_INSERT_SQL).id("sql Insert")
                    .endChoice();


            from("direct:finishCustomer")
                    .delay(2000)
                    .log("Delayed finishing of customer import")
                    .to("bean:beanService?method=updateFileLogFinished")
                    .to("jpa:com.inenergis.entity.log.FileProcessorLog")
                    .to("sql:SET foreign_key_checks=1;")
                    .to("sql:SET unique_checks=1");
        }
    }
}