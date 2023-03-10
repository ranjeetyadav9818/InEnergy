package com.inenergis.controller.program;

import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.service.ProgramAggregatorService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Getter
@Setter
public class CreateAggregator implements Serializable{
	
	@Inject
	ProgramAggregatorService aggregatorService;
	@Inject
	UIMessage uiMessage;
	 
	Logger log =  LoggerFactory.getLogger(CreateAggregator.class);
	
	private ProgramAggregator aggregator = new ProgramAggregator();

	public void save(){
		try {
			aggregatorService.saveAggregator(aggregator);
			uiMessage.addMessage("Aggregator "+aggregator.getName()+" saved");
		} catch (Exception e) {
			uiMessage.addMessage("Error trying to save aggregator, please try again later and contact your administrator if you keep having this problem");
			log.warn("Error saving an aggregator", e);
		}
		aggregator = new ProgramAggregator();
	}
}
