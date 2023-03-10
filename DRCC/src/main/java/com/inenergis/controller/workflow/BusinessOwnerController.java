package com.inenergis.controller.workflow;

import com.inenergis.entity.workflow.BusinessOwner;
import com.inenergis.model.GeneralSelectableDataModel;
import com.inenergis.service.BusinessOwnerService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.Identity;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class BusinessOwnerController implements Serializable{
	
	@Inject
	BusinessOwnerService ownerService;

	@Inject
	Identity identity;

	@Inject
	UIMessage uiMessage;
	 
	Logger log =  LoggerFactory.getLogger(BusinessOwnerController.class);

	private BusinessOwner selectedOwner;
	private BusinessOwner businessOwner;
	private boolean newBusinessOwner;
	private List<BusinessOwner> list;
	private String filterName;
	private String filterEmail;

	@PostConstruct
	public void onCreate() {
		list = ownerService.getAll();
	}

	public void add() {
		businessOwner = new BusinessOwner();
		newBusinessOwner = true;
	}

	public void save() {
		ownerService.save(businessOwner);
		list.add(businessOwner);
		uiMessage.addMessage("Business Owner {0} saved", businessOwner.getName());
		this.cancelBusinessOwner();
	}

	public void cancelBusinessOwner() {
		this.businessOwner = null;
		newBusinessOwner = false;
	}

	public void onRowEdit(RowEditEvent event) {
		BusinessOwner busOwner = (BusinessOwner) event.getObject();
		ownerService.save(busOwner);
	}

	public void onRowCancel(RowEditEvent event) {
		this.businessOwner = (BusinessOwner) event.getObject();
	}


}