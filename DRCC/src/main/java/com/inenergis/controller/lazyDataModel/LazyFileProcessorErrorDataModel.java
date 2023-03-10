package com.inenergis.controller.lazyDataModel;


import com.inenergis.entity.log.FileProcessorError;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyFileProcessorErrorDataModel extends LazyIdentifiableEntityDataModel<FileProcessorError>{

    public LazyFileProcessorErrorDataModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, FileProcessorError.class,preFilters);
    }



}
