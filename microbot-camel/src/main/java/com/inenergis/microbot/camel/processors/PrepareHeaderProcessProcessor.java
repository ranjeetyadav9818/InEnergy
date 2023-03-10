package com.inenergis.microbot.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;

public class PrepareHeaderProcessProcessor implements Processor{
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("customersForUnenrolling", new ArrayList<>());
    }
}
