package com.inenergis.microbot.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyInspector implements Processor {

    Logger log = LoggerFactory.getLogger(BodyInspector.class);

    String level = "";

    public BodyInspector(String level) {
        this.level = level;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.trace(String.format("%s Body %s class %s", level, exchange.getIn().getBody(), exchange.getIn().getBody().getClass()));
        if (exchange.getIn().getHeaders() != null) {
            log.trace("Headers " + exchange.getIn().getHeaders());
        }
        if (exchange.getIn().getBody() instanceof ArrayList) {
            ArrayList list = (ArrayList) exchange.getIn().getBody();
            for (Object obj : list) {
                log.trace(String.format(" item: %s class %s", obj, obj != null ? obj.getClass() : "null"));
                if (obj != null) {
                    if (obj instanceof HashMap) {
                        HashMap map = (HashMap) obj;
                        log.trace(String.format("Map %s %s", map.keySet(), map.values()));
                    }
                }
            }
        }
    }
}