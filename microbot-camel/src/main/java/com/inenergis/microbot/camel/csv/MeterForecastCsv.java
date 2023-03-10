package com.inenergis.microbot.camel.csv;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@CsvRecord(separator = ",")
public class MeterForecastCsv implements Serializable{
    
    @DataField(pos = 1)
    public String serviceAgreementId;

    @DataField(pos = 2)
    public String measureType;

    @DataField(pos = 3 , pattern = "yyyy-MM-dd")
    public Date measureDate;

    @DataField(pos = 4)
    public String hourEnd1;

    
    @DataField(pos = 5)
    public String hourEnd2;

    
    @DataField(pos = 6)
    public String hourEnd3;

    
    @DataField(pos = 7)
    public String hourEnd4;

    
    @DataField(pos = 8)
    public String hourEnd5;

    
    @DataField(pos = 9)
    public String hourEnd6;

    
    @DataField(pos = 10)
    public String hourEnd7;

    
    @DataField(pos = 11)
    public String hourEnd8;

    
    @DataField(pos = 12)
    public String hourEnd9;

    
    @DataField(pos = 13)
    public String hourEnd10;

    
    @DataField(pos = 14)
    public String hourEnd11;

    
    @DataField(pos = 15)
    public String hourEnd12;

    
    @DataField(pos = 16)
    public String hourEnd13;

    
    @DataField(pos = 17)
    public String hourEnd14;

    
    @DataField(pos = 18)
    public String hourEnd15;

    
    @DataField(pos = 19)
    public String hourEnd16;

    
    @DataField(pos = 20)
    public String hourEnd17;


    @DataField(pos = 21)
    public String hourEnd18;


    @DataField(pos = 22)
    public String hourEnd19;


    @DataField(pos = 23)
    public String hourEnd20;


    @DataField(pos = 24)
    public String hourEnd21;


    @DataField(pos = 25)
    public String hourEnd22;


    @DataField(pos = 26)
    public String hourEnd23;

    @DataField(pos = 27)
    public String hourEnd24;

}
