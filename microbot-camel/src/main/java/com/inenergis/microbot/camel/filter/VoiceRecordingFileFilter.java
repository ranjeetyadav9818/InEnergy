package com.inenergis.microbot.camel.filter;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoiceRecordingFileFilter<T> implements GenericFileFilter<T> {

    private static final Logger log = LoggerFactory.getLogger(VoiceRecordingFileFilter.class);

    @Override
    public boolean accept(GenericFile<T> genericFile) {
        log.info("checking for voice files: " + genericFile.getFileNameOnly());
        return genericFile.getFileNameOnly().contains("Recording");
    }
}
