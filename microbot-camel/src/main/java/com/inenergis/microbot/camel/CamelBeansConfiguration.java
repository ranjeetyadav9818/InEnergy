package com.inenergis.microbot.camel;

import com.inenergis.microbot.camel.beans.FileSorter;
import com.inenergis.microbot.camel.filter.VoiceRecordingFileFilter;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by Antonio on 12/10/2017.
 */
@Configuration
public class CamelBeansConfiguration {

    @Bean(name = "fileStoreCDW")
    public FileIdempotentRepository fileStoreCDW(){
        return generateIdempotentRepository("work/drcc/fileidempotent/.filestore.cdw.dat");
    }

    @Bean(name = "fileStorePreferences")
    public FileIdempotentRepository fileStorePreferences(){
        FileIdempotentRepository idempotentRepository = generateIdempotentRepository("work/drcc/fileidempotent/.filestore.preferences.dat");
        return idempotentRepository;
    }

    @Bean(name = "fileStoreEvents")
    public FileIdempotentRepository fileStoreEvents(){
        FileIdempotentRepository idempotentRepository = generateIdempotentRepository("work/drcc/fileidempotent/.filestore.events.dat");
        return idempotentRepository;
    }
    @Bean(name = "fileStoreNewCDW")
    public FileIdempotentRepository fileStoreNewCDW(){
        FileIdempotentRepository idempotentRepository = generateIdempotentRepository("work/drcc/fileidempotent/.filestore.newcdw.dat");
        return idempotentRepository;
    }
    @Bean(name = "fileStoreForecast")
    public FileIdempotentRepository fileStoreForecast(){
        FileIdempotentRepository idempotentRepository = generateIdempotentRepository("work/drcc/fileidempotent/.filestore.forecast.dat");
        return idempotentRepository;
    }
    private FileIdempotentRepository generateIdempotentRepository(String pathname) {
        FileIdempotentRepository idempotentRepository = new FileIdempotentRepository();
        idempotentRepository.setCacheSize(250000);
        idempotentRepository.setMaxFileStoreSize(512000000L);
        idempotentRepository.setFileStore(new File(pathname));
        return idempotentRepository;
    }

    @Bean(name = "cvsFileSorter")
    public FileSorter cvsFileSorter(){
        return new FileSorter();
    }

    @Bean(name = "recordingsFileFilter")
    public VoiceRecordingFileFilter<Object> recordingsFileFilter(){
        return new VoiceRecordingFileFilter<>();
    }

}
