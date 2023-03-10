package com.inenergis.microbot.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class OriginalFileNameSetterProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = (String) exchange.getIn().getHeader("fileName");
        exchange.getIn().setHeader("fileName", fileName.split("-_-")[0]);
    }
}
