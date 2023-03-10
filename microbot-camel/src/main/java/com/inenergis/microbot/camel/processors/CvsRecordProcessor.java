package com.inenergis.microbot.camel.processors;

import com.inenergis.microbot.camel.csv.CvsRecord;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CvsRecordProcessor implements Processor {

    Logger log = LoggerFactory.getLogger(CvsRecordProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        if (body instanceof HashMap) {
            HashMap map = (HashMap) body;
            for (Object key : map.keySet()) {
                log.trace("Key " + key);
                Object obj = map.get(key);
                if (obj instanceof CvsRecord) {
                    CvsRecord rec = (CvsRecord) obj;
                    log.trace(rec.toString());
                    exchange.getOut().setBody(rec.getSqlInsert());
                }
            }
        }
    }
}