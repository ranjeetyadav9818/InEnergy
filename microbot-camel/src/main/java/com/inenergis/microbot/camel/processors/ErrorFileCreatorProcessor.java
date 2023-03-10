package com.inenergis.microbot.camel.processors;


import com.inenergis.entity.log.FileProcessorError;
import com.inenergis.entity.log.FileProcessorLog;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ErrorFileCreatorProcessor implements Processor {

    public static final String ORIGINAL_MESSAGE = "originalMessage";
    public static final int MAX_WIDTH = 250;

    @Override
    public void process(Exchange exchange) throws Exception {
        FileProcessorError error = new FileProcessorError();
        error.setResolved(false);
        error.setRowData(((String) exchange.getIn().getHeader(ORIGINAL_MESSAGE)).getBytes());
        error.setFileProcessorLog(((FileProcessorLog) exchange.getIn().getHeader("fileProcessorLog")));
        Exception exception = ((Exception) exchange.getIn().getHeader("exception"));
        if (exception.getCause() != null) {
            error.setError(StringUtils.abbreviate(exception.getCause().getMessage(),0, MAX_WIDTH));
        } else {
            error.setError(StringUtils.abbreviate(exception.getMessage(),0,MAX_WIDTH));
        }
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        error.setExceptionDetail(sw.toString().getBytes());
        error.setFileProcessorLog(((FileProcessorLog) exchange.getIn().getHeader("fileProcessorLog")));
        exchange.getIn().setBody(error);
    }
}
