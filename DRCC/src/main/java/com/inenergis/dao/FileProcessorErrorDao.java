package com.inenergis.dao;

import com.inenergis.entity.log.FileProcessorError;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class FileProcessorErrorDao extends GenericDao<FileProcessorError>{
    public FileProcessorErrorDao(){
        this.setClazz(FileProcessorError.class);
    }
}

