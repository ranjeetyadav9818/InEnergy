package com.inenergis.microbot.camel.services;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.entity.workflow.SystemTaskExecutor;
import com.inenergis.entity.workflow.TaskInstance;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.microbot.camel.dao.TaskInstanceDao;
import com.inenergis.util.VelocityUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
@Service
public class WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    @Autowired
    private TaskInstanceDao taskInstanceDao;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    @Autowired
    private JMSUtilContract jmsUtil;

    @Transactional
    public void findAllErrorInstances(Exchange exchange) throws Exception {
        List<TaskInstance> taskList = taskInstanceDao.getAllByStatus(TaskStatus.ERROR);
        for (TaskInstance taskInstance : taskList) {
            taskInstance.getPlanInstance().getTaskInstances().forEach(t -> t.getNotificationInstanceList().isEmpty());
            taskInstance.getTask().getBusinessOwner().getNotificationDefinitions().isEmpty();
        }
        exchange.getIn().setBody(taskList);
    }

    public void retryInstance(Exchange exchange) throws Exception {
        TaskInstance instance = (TaskInstance) exchange.getIn().getBody();
        SystemTaskExecutor.execute(instance, appProperties,jmsUtil);
        instance.setStatus(TaskStatus.COMPLETED);
        instance.setActualEnd(new Date());
        instance.setElapsedTime(Duration.between(instance.getStart().toInstant(), instance.getActualEnd().toInstant()).getSeconds());

        (new WorkflowEngine()).manageTaskInstanceTransitions(instance.getPlanInstance(), instance, new VelocityUtil(), appProperties, jmsUtil);
        exchange.getIn().setBody(instance.getPlanInstance());
    }
}