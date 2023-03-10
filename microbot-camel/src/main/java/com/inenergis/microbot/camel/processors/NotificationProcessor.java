package com.inenergis.microbot.camel.processors;

import com.inenergis.microbot.camel.csv.Notification;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationProcessor implements Processor{

	static Logger log = LoggerFactory.getLogger(NotificationProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		//if(Debug.doDebug)log.info("Original input "+body+" class "+body.getClass());
		if(body instanceof HashMap){
			Map m = (Map)body;
			if(!m.isEmpty()){
				body = m.values().iterator().next();
			}else{
				return;
			}
		}

		Notification n = (Notification)body;
		log.trace("Process Notification  "+n.getRecId()+" eventId "+n.getEventId()+" participant "+n.getParticipantId());
		
		List<Object> list = Arrays.asList(
				(Object)n.getPrefCategory(),
				n.getRecId(),
				n.getMessageTemplate(),
				n.getPersonId(),
				n.getNotifyBy(),
				n.getCleanedNotifyByValue(),
				n.getLanguage(),
				n.getCreationTimestamp(),
				n.getEventDisplayName(),
				n.getEventDisplayDate(),
				n.getEventCancelStatus(),
				n.getEventDisplayPremiseAddress(),
				n.getSecondDisplayAddress(),
				n.getAdditionalAddressCount(),
				n.getVendorStatus(),
				n.getPdpReservationCapacity(),
				n.getEventId(),
				n.getParticipantId(),
				n.getVendorId(),
				exchange.getIn().getHeader("CSV_LINE"),
				n.getCreationTimestamp());
		
		exchange.getIn().setBody(list, List.class);
	}
}