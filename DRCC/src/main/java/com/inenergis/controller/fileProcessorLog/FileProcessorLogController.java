package com.inenergis.controller.fileProcessorLog;


import com.inenergis.controller.lazyDataModel.LazyFileProcessorErrorDataModel;
import com.inenergis.controller.lazyDataModel.LazyFileProcessorLogDataModel;
import com.inenergis.entity.log.FileProcessorError;
import com.inenergis.entity.log.FileProcessorLog;
import com.inenergis.service.FileProcessorLogService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class FileProcessorLogController implements Serializable {

    Logger log = LoggerFactory.getLogger(FileProcessorLogController.class);

    @Inject
    FileProcessorLogService fileProcessorLogService;

    @Inject
    EntityManager entityManager;

    @Inject
    UIMessage uiMessage;

    LazyFileProcessorErrorDataModel lazyFileProcessorErrors;

    LazyFileProcessorLogDataModel lazyFileProcessorLogs;

    boolean selectedFile = false;

    Map<String, Object> logPreFilter;
    Map<String, Object> errorPreFilter;

    @PostConstruct
    public void init() {
        logPreFilter = generateLogPreFilters();
        lazyFileProcessorLogs = new LazyFileProcessorLogDataModel(entityManager, logPreFilter);
    }

    private Map<String, Object> generateLogPreFilters() {
        Map<String, Object> prefFilters = new HashMap<String, Object>();
        return prefFilters;
    }

    public void onSelectFile(SelectEvent event) {
        FileProcessorLog fileProcessorLog = (FileProcessorLog) event.getObject();
        if (fileProcessorLog.getFileProcessorErrors().size()==0) {
            uiMessage.addMessage("File without errors");
        }
        errorPreFilter = generateErrorPreFilter(fileProcessorLog.getId());
        lazyFileProcessorErrors = new LazyFileProcessorErrorDataModel(entityManager, errorPreFilter);
        selectedFile = true;
    }

    public void onSolveError(Long id) {
        FileProcessorError fileProcessorError = fileProcessorLogService.getErrorById(id);
        fileProcessorError.setResolved(true);
        fileProcessorLogService.saveError(fileProcessorError);
    }


    private Map<String, Object> generateErrorPreFilter(Long id) {
        Map<String, Object> prefFilters = new HashMap<String, Object>();
        prefFilters.put("fileProcessorLog.id", id);
        return prefFilters;
    }


    public StreamedContent rowDataAsFile(String uuid) {
        FileProcessorError error = lazyFileProcessorErrors.getRowData(uuid);
        return new DefaultStreamedContent(new ByteArrayInputStream(error.getRowData()), "text/plain", error.getFileProcessorLog().getFilename() + uuid);
    }


}
