package com.inenergis.microbot.camel.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class MapMergeAggregationStrategy implements AggregationStrategy {
	
	Logger log = LoggerFactory.getLogger(MapMergeAggregationStrategy.class);

	private String mergeHeaderName;
	
	public MapMergeAggregationStrategy(String headerName){
		this.mergeHeaderName = headerName;
	}

	@Override
	public Exchange aggregate(Exchange original, Exchange resouce) {
		HashMap<String,Object> originalMap = original.getIn().getBody(HashMap.class);
		
		Object resourceObj = resouce.getIn().getBody();
		originalMap.put(mergeHeaderName, resourceObj);

		return original;
	}
}