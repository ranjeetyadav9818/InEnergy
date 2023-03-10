package com.inenergis.microbot.camel.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringBodyAggregationStrategy implements AggregationStrategy {
	 
	private long lineCount = 0;
	
	Logger log = LoggerFactory.getLogger(StringBodyAggregationStrategy.class);
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		if(oldExchange == null) {
			newExchange.getIn().setBody(new StringBuilder(newExchange.getIn().getBody(String.class)));
			lineCount = 1;
			return newExchange;
		}
		oldExchange.getIn().getBody(StringBuilder.class).append(newExchange.getIn().getBody(String.class));
		lineCount++;
		oldExchange.getIn().getHeaders().put("LINE_COUNT", lineCount);
		return oldExchange;
	}
}