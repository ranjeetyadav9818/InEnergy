package com.inenergis.microbot.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Pojo2Map implements Processor {

	Logger log = LoggerFactory.getLogger(Pojo2Map.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String,Object> map = new HashMap<>();
		Object body = exchange.getIn().getBody();
		log.trace("Original input "+body+" class "+body.getClass());
		if(body instanceof HashMap){
			Map m = (Map)body;
			if(!m.isEmpty()){
				body = m.values().iterator().next();
			}else{
				return;
			}
		}
		log.trace("Input "+body.getClass());
		BeanMap beanMap = new BeanMap(body);
		for(Entry<Object,Object> entry: beanMap.entrySet()){
			map.put((String)entry.getKey(), entry.getValue());
		}
		map.put("UUID", UUID.randomUUID().toString());
		exchange.getOut().setBody(map);
	}
}