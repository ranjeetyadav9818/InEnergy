package com.inenergis.microbot.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopRouteProcessor implements Processor {
	
	private String routeName;

	public StopRouteProcessor(String routeName){
		this.routeName = routeName;
	}

	Logger log = LoggerFactory.getLogger(StopRouteProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getContext().getInflightRepository().remove(exchange);
		log.info("Stopping route "+routeName);
		exchange.getContext().stopRoute(routeName);
		
	}
}