package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.log.FileProcessorLog;

import javax.persistence.EntityManager;
import java.util.Map;


public class LazyFileProcessorLogDataModel extends LazyIdentifiableEntityDataModel<FileProcessorLog> {


    public LazyFileProcessorLogDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, FileProcessorLog.class,preFilters);
    }
}
