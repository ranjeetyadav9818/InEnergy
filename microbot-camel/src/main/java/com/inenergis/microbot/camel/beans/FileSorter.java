package com.inenergis.microbot.camel.beans;

import org.apache.camel.component.file.GenericFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSorter implements Comparator<GenericFile> {

    Logger log = LoggerFactory.getLogger(FileSorter.class);


    public static final String pattern = "([a-zA-Z]{2,3})_(create|cancel)_([a-zA-Z_]*)_([0-9]{8}).csv";
    Pattern p = Pattern.compile(pattern);

    public static final int PROGRAM_INDEX = 1;
    public static final int ACTION_TYPE_INDEX = 2;
    public static final int FILE_TYPE_INDEX = 3;
    public static final int DATE_INDEX = 4;


    Map<String, Integer> fileSortMap = Collections.unmodifiableMap(Stream.of(
            new SimpleEntry<>("details", 0),
            new SimpleEntry<>("participants", 1),
            new SimpleEntry<>("exceptions", 2),
            new SimpleEntry<>("notifications_fax", 6),
            new SimpleEntry<>("notifications_mail", 4),
            new SimpleEntry<>("notifications_text", 5),
            new SimpleEntry<>("notifications_voice", 3),
            new SimpleEntry<>("statistics", 7),
            new SimpleEntry<>("trigger", 8))
            .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

    @Override
    public int compare(GenericFile o1, GenericFile o2) {
        log.trace("Comparing " + o1.getFileName() + " to " + o2.getFileName());
        Matcher m1 = p.matcher(o1.getFileName());
        Matcher m2 = p.matcher(o2.getFileName());
        if (m1.find() && m2.find()) {
            String date1 = m1.group(DATE_INDEX);
            String action1 = m1.group(ACTION_TYPE_INDEX);
            String event1 = m1.group(PROGRAM_INDEX);
            String type1 = m1.group(FILE_TYPE_INDEX);
            String date2 = m2.group(DATE_INDEX);
            String action2 = m2.group(ACTION_TYPE_INDEX);
            String event2 = m2.group(PROGRAM_INDEX);
            String type2 = m2.group(FILE_TYPE_INDEX);
            //first compare the date
            int ret = date1.compareTo(date2);
            if (ret != 0) {
                return ret;
            }
            ret = event1.compareTo(event2);
            if (ret != 0) {
                return ret;
            }
            //create before cancel (that's why action2.compareTo(action1) and not the other way round
            ret = action2.compareTo(action1);
            if (ret != 0) {
                return ret;
            }
            Integer i1 = fileSortMap.get(type1);
            Integer i2 = fileSortMap.get(type2);
            if (i1 != null && i2 != null) {
                return i1 - i2;
            } else if (i1 == null) {
                return 1;
            } else if (i2 == null) {
                return -1;
            }
            return 0;
        }

        return o1.getFileName().compareTo(o2.getFileName());
    }
}
