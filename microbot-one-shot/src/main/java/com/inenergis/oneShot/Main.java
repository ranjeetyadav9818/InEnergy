package com.inenergis.oneShot;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Main {

    public static void main(String ... args) throws IOException {

        Reader in = new FileReader("/Users/Antonio/Downloads/names.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
        for (CSVRecord record : records) {
            String id = getId(record, 0);
            String firstName = record.get(1);
            String lastName = record.get(2);
            String address = record.get(3);
            String city = record.get(4);
            String zipCode = record.get(5);
            String state = record.get(6);
            String phone = record.get(7);
            String lat = record.get(8);
            String longitude = record.get(9);
            // System.out.println("UPDATE PREMISE SET SERVICE_ADDRESS1 = '"+address+"', SERVICE_CITY_UPR='"+city+"', SERVICE_STATE='"+state+"', SERVICE_POSTAL='"+zipCode+"', COUNTY=NULL WHERE RIGHT(PREMISE_ID,3)='"+id+"';");
            // System.out.println("UPDATE PERSON SET CUSTOMER_NAME = '"+firstName+" "+lastName+"' WHERE RIGHT(PERSON_ID,3)='"+id+"';");
            // System.out.println("UPDATE SERVICE_POINT SET LATITUDE = '"+lat+"', LONGITUDE='"+longitude+"' WHERE RIGHT(SERVICE_POINT_ID,3)='"+id+"';");
            // System.out.println("UPDATE SERVICE_AGREEMENT SET MAILING_ADDRESS1 = '"+address+"', MAILING_ADDRESS2=NULL, MAILING_CITY_UPR='"+city+"', MAILING_STATE='"+state+"', MAILING_POSTAL='"+zipCode+"', PHONE='"+phone.replace("-","")+"', CUSTOMER_LSE_COMPANY_NAME='Sacramento Municipal Utility District', CUSTOMER_LSE_COMPANY_NAME='SMUD' WHERE RIGHT(SERVICE_AGREEMENT_ID,3)='"+id+"';");
        }
    }

    private static String getId(CSVRecord record, int i) {
        String s = record.get(i).trim();
        while (s.length()<3){
            s = "0" + s;
        }
        return s;
    }
}
