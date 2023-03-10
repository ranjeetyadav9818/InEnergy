package com.inenergis.controller.workflow;

import com.inenergis.entity.workflow.BusinessOwner;
import com.inenergis.entity.workflow.NotificationDefinition;
import com.inenergis.service.BusinessOwnerService;
import com.inenergis.service.NotificationDefinitionService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
public class NotificationDefinitionController implements Serializable{
	
	@Inject
	NotificationDefinitionService notificationDefinitionService;

	@Inject
    BusinessOwnerService businessOwnerService;

	@Inject
	UIMessage uiMessage;
	 
	Logger log =  LoggerFactory.getLogger(NotificationDefinitionController.class);

	private NotificationDefinition notificationDefinition;
	private boolean newNotificationDefinition;
	private List<BusinessOwner> businessOwners;
	private List<NotificationDefinition> list;
    private String name;
	private String businessOwnerName;

	@PostConstruct
	public void onCreate() {
        businessOwners = businessOwnerService.getAll();
        list = notificationDefinitionService.getAll();
	}

	public void save() {
		notificationDefinitionService.save(notificationDefinition);
		list.add(notificationDefinition);
		uiMessage.addMessage("Alert {0} saved", notificationDefinition.getDescription());
        this.cancel();
	}

	public void cancel() {
		this.notificationDefinition = null;
		newNotificationDefinition = false;
	}

	public void onRowEdit(RowEditEvent event) {
		NotificationDefinition notificationDefinition = (NotificationDefinition) event.getObject();
		notificationDefinitionService.save(notificationDefinition);
	}

	public void onRowCancel(RowEditEvent event) {
		this.notificationDefinition = (NotificationDefinition) event.getObject();
	}


}