package com.inenergis.microbot.camel.beans;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileFilter<T> implements GenericFileFilter<T> {

    private String currentEvent;
    private String currentProgram;

    Logger log = LoggerFactory.getLogger(FileFilter.class);

    Pattern p = Pattern.compile(FileSorter.pattern);

    @Override
    public boolean accept(GenericFile<T> file) {
        Matcher m = p.matcher(file.getFileName());
        log.trace("checking " + file.getFileName());
        if (m.find()) {
            String program = m.group(1);
            String type = m.group(2);
            String event = m.group(3);

            if ("details".equalsIgnoreCase(type)) {
                log.warn("Start of new event " + program + " " + event);
                currentEvent = event;
                currentProgram = program;
            } else {
                if (currentProgram.equals(program)) {
                    return true;
                } else {
                    log.error("Different program not starting yet");
                    return false;
                }
            }
        }
        return false;
    }
}