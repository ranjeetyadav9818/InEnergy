package com.inenergis.controller.workflow;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.controller.lazyDataModel.LazyNotificationInstanceDataModel;
import com.inenergis.controller.lazyDataModel.LazyTaskInstanceDataModel;
import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.entity.workflow.BusinessOwner;
import com.inenergis.entity.workflow.NotificationInstance;
import com.inenergis.entity.workflow.TaskInstance;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.service.MailService;
import com.inenergis.service.NotificationInstanceService;
import com.inenergis.service.TaskInstanceService;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.UIMessage;
import com.inenergis.util.VelocityUtil;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class MonitorController implements Serializable {

    @Inject
    private NotificationInstanceService notificationInstanceService;

    @Inject
    private TaskInstanceService taskInstanceService;

    @Inject
    private WorkPlanServiceContract workPlanService;

    @Inject
    private EntityManager entityManager;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private MailService mailService;

    @Inject
    private PropertyAccessor propertyAccessor;

    @Inject
    private JMSUtilContract jmsUtil;

    private Logger log = LoggerFactory.getLogger(MonitorController.class);

    private LazyNotificationInstanceDataModel lazyNotificationInstanceDataModel;
    private Map<String, Object> notificationInstancePreFilter;
    private LazyTaskInstanceDataModel lazyTaskInstanceDataModel;
    private Map<String, Object> taskInstancePreFilter;
    private BusinessOwner businessOwner;
    private DefaultMenuModel userActions;
    private boolean showTables;

    @PostConstruct
    public void init() {
        userActions = new DefaultMenuModel();
        DefaultMenuItem cancel = new DefaultMenuItem("Cancel");
        cancel.setCommand("#{monitorController.cancelTask(taskInstance)}");
        cancel.setUpdate("@form");
        userActions.addElement(cancel);
        DefaultMenuItem completed = new DefaultMenuItem("Completed");
        completed.setCommand("#{monitorController.completedTask(taskInstance)}");
        completed.setUpdate("@form");
        userActions.addElement(completed);
        search();
    }

    public void onWorkflowEdit(RowEditEvent event) {
        TaskInstance taskInstance = (TaskInstance) event.getObject();
        taskInstance.setLastUpdated(new Date());
        taskInstanceService.save(taskInstance);
    }

    public void onWorkflowCancel(RowEditEvent event) {
    }

    private Map<String, Object> generateNotificationInstancesPreFilter() {
        Map<String, Object> preFilter = new HashMap<>();
        if (businessOwner != null) {
            preFilter.put("businessOwner.id", businessOwner.getId());
        }
        return preFilter;
    }

    private Map<String, Object> generateTaskInstancesPreFilter() {
        Map<String, Object> preFilter = new HashMap<>();
        if (businessOwner != null) {
            preFilter.put("task.businessOwner.id", businessOwner.getId());
        }
        return preFilter;
    }

    public void search() {
        notificationInstancePreFilter = generateNotificationInstancesPreFilter();
        lazyNotificationInstanceDataModel = new LazyNotificationInstanceDataModel(entityManager, notificationInstancePreFilter);
        taskInstancePreFilter = generateTaskInstancesPreFilter();
        lazyTaskInstanceDataModel = new LazyTaskInstanceDataModel(entityManager, taskInstancePreFilter);
        showTables = true;
    }

    public void save(NotificationInstance notificationInstance) {
        notificationInstanceService.save(notificationInstance);
    }


    public void completedTask(TaskInstance taskInstance) {
        taskInstance.setStatus(TaskStatus.COMPLETED);
        taskInstanceService.save(taskInstance);
        (new WorkflowEngine()).manageTaskInstanceTransitions(taskInstance.getPlanInstance(), taskInstance, new VelocityUtil(), propertyAccessor.getProperties(), jmsUtil);
        workPlanService.savePlanInstance(taskInstance.getPlanInstance());
    }


    public void cancelTask(TaskInstance taskInstance) {
        taskInstance.setStatus(TaskStatus.CANCELLED);
        taskInstanceService.save(taskInstance);
        (new WorkflowEngine()).manageTaskInstanceTransitions(taskInstance.getPlanInstance(), taskInstance, new VelocityUtil(), propertyAccessor.getProperties(), jmsUtil);
        workPlanService.savePlanInstance(taskInstance.getPlanInstance());
    }
}