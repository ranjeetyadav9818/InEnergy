package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.csv.CustomerForMarshalling;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FakeDataGenerator extends RouteBuilder {

    private static final String CUSTOMER_NAME = "Tesla";

    private static Map<String, String> countyMap = new HashMap<>();

    static {
        countyMap.put("SFO","SAN FRANCISCO");
        countyMap.put("SJO","SANTA CLARA");
        countyMap.put("EBA","ALAMEDA");
        countyMap.put("KER","KERN");
        countyMap.put("YOS","MADERA");
        countyMap.put("FRE","FRESNO");
        countyMap.put("NBY","MARIN");
    }


    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat bindyCustomer = new BindyCsvDataFormat(CustomerForMarshalling.class);
        Processor processor = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                List<CustomerForMarshalling> customers = new ArrayList<>();
                List<FakeDataCSV> list = getList(((GenericFile) exchange.getIn().getBody()), mapToFakeData);
                for (FakeDataCSV datum : list) {
                    customers.add(convertToCustomer(datum));
                }
                exchange.getIn().setBody(customers);
            }
        };
        Processor processor1 = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn();
            }
        };
        from("file:work/drcc/fake/")
                .process(processor)
                .marshal(bindyCustomer)
                .to("file:work/drcc/result/fake");
    }

    private <T extends Object> List<T> getList(GenericFile inputF, Function<String, T> function) {
        try{
            InputStream inputFS = new FileInputStream(new File(inputF.getAbsoluteFilePath()));
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            List collect = br.lines().skip(1).map(function).filter(p -> p!=null).collect(Collectors.toList());
            br.close();
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Function<String, FakeDataCSV> mapToFakeData = (line) -> {
        String[] p = line.split(",");// a CSV has comma separated lines
        if(p.length>10){
            return null;
        }
        FakeDataCSV item = new FakeDataCSV();
        item.setNumber(p[0]);
        item.setSurname(p[1]);
        item.setFirstName(p[2]);
        item.setStreetAddress(p[3]);
        item.setCity(p[4]);
        item.setState(p[5]);
        item.setZipCode(p[6]);
        item.setTelephoneNumber(p[7]);
        item.setLatitude(Double.parseDouble(p[8]));
        item.setLongitude(Double.parseDouble(p[9]));
        return item;
    };

    private CustomerForMarshalling convertToCustomer(FakeDataCSV datum) {
        CustomerForMarshalling customer = new CustomerForMarshalling();
        customer.setSA_STATUS("20");
        customer.setSA_ID(generateRandomId());
        customer.setACCOUNT_ID(generateRandomId());
        customer.setPREMISE_ID(generateRandomId());
        customer.setMETER_ID(generateRandomId());
        customer.setPERSON_ID(generateRandomId());
        customer.setLAST_NAME(datum.getSurname()+","+datum.getFirstName());
        customer.setSERVICE_ADDRESS1(datum.getStreetAddress());
        customer.setSERVICE_CITY_UPR(datum.getCity());
        customer.setSERVICE_STATE(datum.getState());
        customer.setSERVICE_POSTAL(datum.getZipCode());
        customer.setPHONE(datum.getTelephoneNumber().replace("-",""));
        customer.setMAILING_ADDRESS1(datum.getStreetAddress());
        customer.setMAILING_CITY_UPR(datum.getCity());
        customer.setMAILING_STATE(datum.getState());
        customer.setMAILING_ZIP(datum.getZipCode());
        customer.setRS_CD(generateRandomRateSchedule());
        customer.setRES_YN_IND(generateRandomBoolean(2));
        customer.setSUBSTATION(generateSubstation());
        customer.setFEEDER(generateRandomId().substring(1));
        customer.setSP_ID(generateRandomId());
        customer.setMEDICAL_BASELINE_IND(generateRandomBoolean(100));
        customer.setLIFE_SUPPORT_IND(generateRandomBoolean(100));
        customer.setSMART_RATE_IND("0");
        customer.setSA_SP_START_DTTM(generateDateTimeLastDays(1000));
        customer.setSA_SP_STOP_DTTM(null);
        customer.setPREMISE_TYPE(generatePremiseType());
        customer.setPDP_ENROLLED("0");
        customer.setELEC_USAGE_NONRES(generateRandomBoolean(2));
        customer.setHAS_3RD_PARTY_DRP(generateRandomBoolean(100));
        customer.setPHONE_EXTENSION("NA");
        customer.setSUPPLIER_IS_DRP(generateRandomBoolean(100));
        customer.setUNIQ_SA_ID(generateRandomId());
        customer.setUNIQ_SA_ID_CREATE_DATE(generateDateTimeLastDays(100));
        customer.setUNIQ_SA_ID_WARN_FLAG(generateRandomBoolean(50));
        customer.setSA_UUID(generateRandomId());
        customer.setDO_BUS_AS_NM(generateBusinessAs(customer.getLAST_NAME()));
        customer.setSA_START_DATE(customer.getUNIQ_SA_ID_CREATE_DATE());
        customer.setSA_NAICS(generateRandomId().substring(4));
        customer.setBILL_CYCLE_CD(generateRandomBillCycle());
        customer.setCUST_CLASS_CD(customer.getPREMISE_TYPE());
        customer.setREVENUE_CLASS_DESC(generateRevenueClass());
        customer.setFERA_FLAG(generateRandomBoolean(100));
        customer.setBILL_SYSTEM(generateBillSystem());
        customer.setCUST_SIZE(generateCustSize());
        customer.setMARKET_SEGMENT(generateMarketSegment());
        customer.setCARE_FLAG(generateRandomBoolean(100));
        customer.setOPERATION_AREA(generateOperationArea());
        customer.setPREM_BASELINE_CHAR(generatePremiseBaselineChar());
        customer.setMTR_BADGENBR(generateRandomId());
        customer.setSM_STATUS(generateSMStatus());
        customer.setMTR_INSTALL_DT(customer.getSA_START_DATE());
        customer.setSM_MODULE_MFR(generateSMModule());
        customer.setMTR_CONFIG_TYPE(generateMeterConfigType());
        customer.setMTR_READ_FREQ(ThreadLocalRandom.current().nextInt(2)==0?"15":null);
        customer.setMTR_MFG(generateMeterMfg());
        customer.setPDP_BILL_PROTECTION("0");
        customer.setSR_BILL_PROTECTION("0");
        customer.setSA_TYPE_CD(generateSaTypeCD());
        customer.setCUSTOMER_MDMA_COMPANY_NAME(ThreadLocalRandom.current().nextInt(2)==0?CUSTOMER_NAME:null);
        customer.setCUSTOMER_MSP_COMPANY_NAME(ThreadLocalRandom.current().nextInt(2)==0?CUSTOMER_NAME:null);
        customer.setCUSTOMER_LSE_COMPANY_NAME(CUSTOMER_NAME);
        customer.setCUST_METER_READ_CYCLE12(customer.getBILL_CYCLE_CD());
        customer.setCUST_SERVICE_VOLTAGE_CLASS(generateVoltageClass());
        customer.setSA_SP_ID(generateRandomId());
        customer.setRATE_CODE_EFFECTIVE_DATE(customer.getUNIQ_SA_ID_CREATE_DATE());
        customer.setSERVICE_TYPE("E");
        customer.setBUSINESS_ACTIVITY_DESC("NA");
        customer.setBUS_OWNER(""+ThreadLocalRandom.current().nextInt(10));
        customer.setDIVISION_CODE_19(generateDivision19());
        customer.setCIRCUIT_NUMBER(customer.getFEEDER().substring(5));
        customer.setSUB_STATION_NUMBER(customer.getFEEDER().substring(0,5));
        customer.setCOUNTY(countyMap.get(customer.getDIVISION_CODE_19()));
        customer.setCLIMATE_ZONE("Z0"+ThreadLocalRandom.current().nextInt(10));
        customer.setESS_DIVISION_CODE(customer.getDIVISION_CODE_19());
        customer.setSOURCE_SIDE_DEVICE_NUMBER(""+ThreadLocalRandom.current().nextInt(10_000,100_000));
        customer.setLATITUDE(""+datum.getLatitude());
        customer.setLONGITUDE(""+datum.getLongitude());
        customer.setTRFMR_NUMBER(generateRandomId() + ThreadLocalRandom.current().nextInt(100,1_000));
        customer.setROB_CODE("" + ThreadLocalRandom.current().nextInt(1,10) + generateRandomBillCycle());
        customer.setCUSTOMER_MDMA_CODE(CUSTOMER_NAME+"-MTR");
        customer.setCUSTOMER_MSP_CODE(CUSTOMER_NAME+"-MTR");
        customer.setCUSTOMER_LSE_CODE(CUSTOMER_NAME+"-FS");
        customer.setESSENTIAL_CUSTOMER_FLAG(ThreadLocalRandom.current().nextInt(100)==0?"YES":null);
        return customer;
    }

    private static final String []division19 = new String[]{"SFO","SJO","EBA","KER","YOS","FRE","NBY"};

    private static String generateDivision19() {
        return division19[ThreadLocalRandom.current().nextInt(7)];
    }

    private static final String []voltageClass = new String[]{"S","P","T"};

    private static String generateVoltageClass() {
        return voltageClass[ThreadLocalRandom.current().nextInt(3)];
    }

    private static final String []saTypeCD = new String[]{"PT-E-CI","HE-C&I-M","HE-C&I-S","HE-C&I-L","HE-AG"};

    private static String generateSaTypeCD() {
        return saTypeCD[ThreadLocalRandom.current().nextInt(5)];
    }

    private static final String []meterMfg = new String[]{"GE","LG","PS","AB","TD"};

    private static String generateMeterMfg() {
        return meterMfg[ThreadLocalRandom.current().nextInt(5)];
    }

    private static final String []smType = new String[]{"TOUCPLXCMBWI","KWHKVARHWINT","SM-15-SIMPLE","SM-15-CMPLX2","BIDIRECTWINT","INTDATARCRDR"};

    private static String generateMeterConfigType() {
        return smType[ThreadLocalRandom.current().nextInt(6)];
    }

    private static final String []smModule = new String[]{"MV-Billed","SSN",null};

    private static String generateSMModule() {
        return smModule[ThreadLocalRandom.current().nextInt(3)];
    }

    private static final String []smStatus = new String[]{"SM-Billed","SM-Read","SM-Disabled","Not SM-Enabled"};

    private static String generateSMStatus() {
        return smStatus[ThreadLocalRandom.current().nextInt(4)];
    }

    private static final String []premiseChar = new String[]{"W","T","S","X","R"};

    private static String generatePremiseBaselineChar() {
        return premiseChar[ThreadLocalRandom.current().nextInt(5)];
    }

    private static final String []operationArea = new String[]{"TX","JG","TN","VP","TK","JJ","TC"};

    private static String generateOperationArea() {
        return operationArea[ThreadLocalRandom.current().nextInt(7)];
    }

    private static final String []marketSegment = new String[]{"AG","C&I",};

    private static String generateMarketSegment() {
        return marketSegment[ThreadLocalRandom.current().nextInt(2)];
    }

    private static final String []custSize = new String[]{"20","75","200"};

    private static String generateCustSize() {
        return custSize[ThreadLocalRandom.current().nextInt(3)];
    }

    private static String generateBillSystem() {
        return ThreadLocalRandom.current().nextInt()%2==0? "ABS": "CCB";
    }

    private static final String []revenues = new String[]{"E Commercial Agricultural Pwr","E Sml Commercial & Industrial","E Med Commercial & Industrial","E Lrg Commercial & Industrial","Dummy ABS-For UUT Calc."};

    private static String generateRevenueClass() {
        return revenues[ThreadLocalRandom.current().nextInt(5)];
    }


    private static String generateRandomBillCycle() {
        return billCycle[ThreadLocalRandom.current().nextInt(7)];
    }

    private static final String []billCycle = new String[]{"B","C","D","F","G","H","J"};


    private static String generateBusinessAs(String name) {
        return ThreadLocalRandom.current().nextInt()%100==0? name + " Inc.": null;
    }

    private static final String []premiseTypes = new String[]{"RES","COM/IND","AG","APT","PUMP_AG","RES","COM/IND"};

    private static String generatePremiseType() {
        return premiseTypes[ThreadLocalRandom.current().nextInt(7)];
    }

    private static Date generateDateTimeLastDays(int i) {
        LocalDate date = LocalDate.now();
        return Date.from(date.minusDays(ThreadLocalRandom.current().nextInt(1,i)).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    private static final String []subs = new String[]{"A","B","C","D","E","F","G"};

    private static String generateSubstation() {
        return "SUBSTATION " + subs[ThreadLocalRandom.current().nextInt(7)];
    }

    private static String generateRandomBoolean(int chance) {
        return ThreadLocalRandom.current().nextInt()%chance==0?"1":"0";
    }

    private static final String []rates = new String[]{"E1","H6","HA1X","HE1N","HE6N","A1","HA10SX"};

    private static String generateRandomRateSchedule() {
        return rates[ThreadLocalRandom.current().nextInt(7)];
    }

    private static String generateRandomId() {
        return "" + ThreadLocalRandom.current().nextLong(1_000_000_000, 10_000_000_000L);
    }


    @Getter
    @Setter
    @ToString
    public static class FakeDataCSV{
        private String number,surname,firstName,streetAddress,city,state,zipCode,telephoneNumber;
        private Double latitude,longitude;
    }
}
