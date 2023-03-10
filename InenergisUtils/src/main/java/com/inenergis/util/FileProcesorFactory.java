package com.inenergis.util;

import java.util.NoSuchElementException;

/**
 * Created by egamas on 01/12/2017.
 */
public class FileProcesorFactory {

    public static FileProcessor getFileProcessor(String tableName){
        if (tableName.equalsIgnoreCase("interval_data")) {
            return new IntervalDataFileProcessor();
        } else if (tableName.equalsIgnoreCase("peak_demand_interval_data")){
            return new PeakDemandIntervalDataFileProcessor();
        }
        throw new NoSuchElementException(tableName);
    }

}
