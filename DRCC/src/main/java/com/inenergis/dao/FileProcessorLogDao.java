package com.inenergis.dao;


import com.inenergis.entity.log.FileProcessorLog;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class FileProcessorLogDao extends GenericDao<FileProcessorLog>{

    public FileProcessorLogDao(){
        this.setClazz(FileProcessorLog.class);
    }

}
