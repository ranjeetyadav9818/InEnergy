package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.log.FileProcessorLog;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileProcessorLogDao extends Repository<FileProcessorLog, Long> {

    List<FileProcessorLog> getByFilename(String filename);

    FileProcessorLog save(FileProcessorLog fileProcessorLog);
}