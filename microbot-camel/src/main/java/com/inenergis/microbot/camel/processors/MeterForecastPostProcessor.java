package com.inenergis.microbot.camel.processors;

import com.inenergis.microbot.camel.csv.MeterForecastCsv;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
public class MeterForecastPostProcessor {

    int NUM_DECIMALS = 2;

    public void postProcess(MeterForecastCsv meterForecastCsv) {
        meterForecastCsv.setHourEnd1(storeWithoutDot(meterForecastCsv.getHourEnd1()));
        meterForecastCsv.setHourEnd2(storeWithoutDot(meterForecastCsv.getHourEnd2()));
        meterForecastCsv.setHourEnd3(storeWithoutDot(meterForecastCsv.getHourEnd3()));
        meterForecastCsv.setHourEnd4(storeWithoutDot(meterForecastCsv.getHourEnd4()));
        meterForecastCsv.setHourEnd5(storeWithoutDot(meterForecastCsv.getHourEnd5()));
        meterForecastCsv.setHourEnd6(storeWithoutDot(meterForecastCsv.getHourEnd6()));
        meterForecastCsv.setHourEnd7(storeWithoutDot(meterForecastCsv.getHourEnd7()));
        meterForecastCsv.setHourEnd8(storeWithoutDot(meterForecastCsv.getHourEnd8()));
        meterForecastCsv.setHourEnd9(storeWithoutDot(meterForecastCsv.getHourEnd9()));
        meterForecastCsv.setHourEnd10(storeWithoutDot(meterForecastCsv.getHourEnd10()));
        meterForecastCsv.setHourEnd11(storeWithoutDot(meterForecastCsv.getHourEnd11()));
        meterForecastCsv.setHourEnd12(storeWithoutDot(meterForecastCsv.getHourEnd12()));
        meterForecastCsv.setHourEnd13(storeWithoutDot(meterForecastCsv.getHourEnd13()));
        meterForecastCsv.setHourEnd14(storeWithoutDot(meterForecastCsv.getHourEnd14()));
        meterForecastCsv.setHourEnd15(storeWithoutDot(meterForecastCsv.getHourEnd15()));
        meterForecastCsv.setHourEnd16(storeWithoutDot(meterForecastCsv.getHourEnd16()));
        meterForecastCsv.setHourEnd17(storeWithoutDot(meterForecastCsv.getHourEnd17()));
        meterForecastCsv.setHourEnd18(storeWithoutDot(meterForecastCsv.getHourEnd18()));
        meterForecastCsv.setHourEnd19(storeWithoutDot(meterForecastCsv.getHourEnd19()));
        meterForecastCsv.setHourEnd20(storeWithoutDot(meterForecastCsv.getHourEnd20()));
        meterForecastCsv.setHourEnd21(storeWithoutDot(meterForecastCsv.getHourEnd21()));
        meterForecastCsv.setHourEnd22(storeWithoutDot(meterForecastCsv.getHourEnd22()));
        meterForecastCsv.setHourEnd23(storeWithoutDot(meterForecastCsv.getHourEnd23()));
        meterForecastCsv.setHourEnd24(storeWithoutDot(meterForecastCsv.getHourEnd24()));
    }

    public static String storeWithoutDot(String kWattsDecimal) {
        if (kWattsDecimal == null) {
            kWattsDecimal = StringUtils.EMPTY;
        }
        String trimmedKwatts = kWattsDecimal.trim();
        String[] parts = trimmedKwatts.split("\\.");
        String intPart = StringUtils.EMPTY;
        String decPart = StringUtils.EMPTY;
        if (parts.length > 0) {
            intPart = parts[0];
        }
        if (parts.length > 1) {
            decPart = parts[1];
        }
        String rPaddedDec = (StringUtils.rightPad(decPart, 3, '0').substring(0, 3));
        return intPart + rPaddedDec;
    }
}