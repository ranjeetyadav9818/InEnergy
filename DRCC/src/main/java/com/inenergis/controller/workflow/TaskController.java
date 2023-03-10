package com.inenergis.controller.workflow;

import com.inenergis.entity.genericEnum.TaskType;
import com.inenergis.entity.workflow.BusinessOwner;
import com.inenergis.entity.workflow.Task;
import com.inenergis.service.BusinessOwnerService;
import com.inenergis.service.TaskService;
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
public class TaskController implements Serializable{
	
	@Inject
	TaskService taskService;

	@Inject
    BusinessOwnerService businessOwnerService;

	@Inject
	Identity identity;

	@Inject
	UIMessage uiMessage;
	 
	Logger log =  LoggerFactory.getLogger(TaskController.class);
	
	private Task selectedTask;
	private Task task;
	private boolean newTask;
	private List<Task> list;
	private List<BusinessOwner> businessOwners;
    private String name;
	private String businessOwnerName;
	private int taskDuration;
	private Long taskId;
	private Boolean used;

	@PostConstruct
	public void onCreate() {
	    list = taskService.getAll();
        businessOwners = businessOwnerService.getAll();
	}

	public void add() {
		task = new Task();
		task.setTaskType(TaskType.USER);
		newTask = true;
	}

	public void save() {

        taskService.save(task);
        list.add(task);
        uiMessage.addMessage("Task {0} saved", task.getName());

        this.cancel();
	}

	public void cancel() {
		this.task = null;
		newTask = false;
	}

	public void onRowEdit(RowEditEvent event) {
		Task task = (Task) event.getObject();
		taskService.save(task);
	}

	public void onRowCancel(RowEditEvent event) {
		this.task = (Task) event.getObject();
	}


}