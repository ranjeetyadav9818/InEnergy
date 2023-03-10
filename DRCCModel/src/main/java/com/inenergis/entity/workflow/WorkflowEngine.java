package com.inenergis.entity.workflow;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.entity.genericEnum.IntervalType;
import com.inenergis.entity.genericEnum.NotificationDefinitionField;
import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.entity.genericEnum.TaskType;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.exception.BusinessException;
import com.inenergis.util.VelocityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.common.util.CollectionUtils;

import javax.jms.JMSException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


public class WorkflowEngine {

    public PlanInstance buildProgramPlanInstance(ProgramServiceAgreementEnrollment programServiceAgreementEnrollment, WorkPlan workPlan, VelocityUtil velocityUtil, Properties properties, JMSUtilContract jmsUtil) {

        ProgramPlanInstance planInstance = new ProgramPlanInstance();
        planInstance.setWorkPlan(workPlan);
        planInstance.setStart(new Date());
        planInstance.setStatus(TaskStatus.IN_PROCESS);
        planInstance.setProgramServiceAgreementEnrollment(programServiceAgreementEnrollment);

        List<TaskInstance> taskInstances = buildTaskInstances(workPlan, planInstance);
        planInstance.setTaskInstances(taskInstances);
        init(planInstance, velocityUtil, properties, jmsUtil);
        return planInstance;
    }

    public PlanInstance buildRatePlanInstance(RatePlanEnrollment enrollment, WorkPlan workPlan, VelocityUtil velocityUtil, Properties properties, JMSUtilContract jmsUtil) {

        RatePlanInstance planInstance = new RatePlanInstance();
        planInstance.setWorkPlan(workPlan);
        planInstance.setStart(new Date());
        planInstance.setStatus(TaskStatus.IN_PROCESS);
        planInstance.setRatePlanEnrollment(enrollment);

        List<TaskInstance> taskInstances = buildTaskInstances(workPlan, planInstance);
        planInstance.setTaskInstances(taskInstances);
        init(planInstance, velocityUtil, properties, jmsUtil);
        return planInstance;
    }

    public static void init(PlanInstance planInstance, VelocityUtil velocityUtil, Properties properties, JMSUtilContract jmsUtil) {
        setFirstTimeDates(planInstance);
        final List<TaskInstance> taskInstances = planInstance.getTaskInstances();
        if (CollectionUtils.isEmpty(taskInstances)) {
            return;
        }
        HashMap<Integer, List<TaskInstance>> taskGroups = orderTasksByGroup(taskInstances);
        triggerNextTasks(velocityUtil, properties, taskGroups, null, jmsUtil);
    }

    private static List<TaskInstance> buildTaskInstances(WorkPlan workPlan, PlanInstance planInstance) {
        final List<WorkPlanTaskList> workPlanTaskLists = workPlan.getWorkPlanTaskLists();
        List<TaskInstance> taskInstances = new ArrayList<>(CollectionUtils.isEmpty(workPlanTaskLists) ? 0 : workPlanTaskLists.size());

        for (WorkPlanTaskList workPlanTaskList : workPlanTaskLists) {
            final Task task = workPlanTaskList.getTask();
            TaskInstance instance = TaskInstance.builder()
                    .planInstance(planInstance)
                    .task(task)
                    .order(workPlanTaskList.getOrder())
                    .status(TaskStatus.PENDING_ACTION)
                    .start(planInstance.getStart())
                    .build();
            taskInstances.add(instance);
        }
        return taskInstances;
    }

    public void manageTaskInstanceTransitions(PlanInstance planInstance, TaskInstance modifiedTask, VelocityUtil velocityUtil, Properties properties, JMSUtilContract jmsUtil) {

        final List<TaskInstance> taskInstances = planInstance.getTaskInstances();
        if (CollectionUtils.isEmpty(taskInstances)) {
            return;
        }
        HashMap<Integer, List<TaskInstance>> taskGroups = orderTasksByGroup(taskInstances);

        triggerNextTasks(velocityUtil, properties, taskGroups, modifiedTask.getParsedOrder().getLeft(), jmsUtil);
        if (checkAllTasksFinished(taskGroups)) {
            //all notifications are finished. Lets see if the plan can be closed
            planInstance.setActualEnd(new Date());
            planInstance.setStatus(TaskStatus.COMPLETED);
            planInstance.setElapsedTotalTime(planInstance.calculateTotalElapsedTime());
            //todo set time elapsed for tasks TimeUtil.calculate....
        }
    }

    private static void triggerNextTasks(VelocityUtil velocityUtil, Properties properties, HashMap<Integer, List<TaskInstance>> taskGroups, Integer left, JMSUtilContract jmsUtil) {
        Pair<Integer, List<TaskInstance>> currentTaskGroupPair = collectMinOrderWithStatusPresentNotNotified(taskGroups, Arrays.asList(TaskStatus.PENDING_ACTION));
        if (currentTaskGroupPair == null) {
            return;
        }
        Pair<Integer, List<TaskInstance>> previousTaskGroupPair = null;
        while (previousTaskGroupPair == null || (currentTaskGroupPair != null && !currentTaskGroupPair.getLeft().equals(previousTaskGroupPair.getLeft()))) {
            previousTaskGroupPair = currentTaskGroupPair;
            if (currentTaskGroupPair != null) {
                final List<TaskInstance> nextTaskGroup = currentTaskGroupPair.getRight();
                if (left == null || checkPreviousTaskGroupCompleted(taskGroups, left)) { // check the previous order are all completed if not first time
                    generateNotifications(nextTaskGroup, velocityUtil);
                    moveStatusesForward(nextTaskGroup, properties, jmsUtil);
                }
            }
            currentTaskGroupPair = collectMinOrderWithStatusPresentNotNotified(taskGroups, Arrays.asList(TaskStatus.PENDING_ACTION, TaskStatus.IN_PROCESS, TaskStatus.ERROR));
        }
    }

    public List<NotificationInstance> pause(PlanInstance planInstance) {
        planInstance.setPauseStart(new Date());
        planInstance.setLastPauseStart(planInstance.getPauseStart());
        planInstance.setResumedTime(null);
        for (TaskInstance taskInstance : planInstance.getTaskInstances()) {
            if (taskInstance.getStatus().equals(TaskStatus.IN_PROCESS)) {
                taskInstance.setStatus(TaskStatus.PAUSED);
            }
        }
        planInstance.setStatus(TaskStatus.PAUSED);
        return generateNotificationsPlanPauseResume(planInstance, new VelocityUtil(), TaskStatus.PAUSED);
    }

    private static List<NotificationInstance> generateNotificationsPlanPauseResume(PlanInstance planInstance, VelocityUtil velocityUtil, TaskStatus newStatus) {
        final String programName = planInstance.getWorkPlan().getProgram().getName();
        final WorkPlanType workPlanType = planInstance.getWorkPlan().getType();
        Map<String, Object> fields = new HashMap(2);
        fields.put("programName", programName);
        fields.put("workPlanType", workPlanType.getName());
        fields.put("newStatus", newStatus.getText());
        final NotificationDefinitionId id = TaskStatus.IN_PROCESS.equals(newStatus) ? NotificationDefinitionId.BAM_RESUME_ALERT : NotificationDefinitionId.BAM_PAUSED_ALERT;
        List<NotificationInstance> notifications = new ArrayList<>();
        final Alert alert = Alert.builder()
                .type(id)
                .reference(Long.toString(planInstance.getId()))
                .messageFields(fields).build();

        //Business owner left as null and determined by the route
        notifications.add(alert.generateNotification(velocityUtil));

        final List<TaskInstance> taskInstances = planInstance.getTaskInstances();
        if (!CollectionUtils.isEmpty(taskInstances)) {
            for (TaskInstance taskInstance : taskInstances) {
                if (NotificationDefinitionId.BAM_RESUME_ALERT.equals(id) && taskInstance.getStatus().equals(TaskStatus.IN_PROCESS)) {
                    //If we are resumning we have to notify the tasks now in process
                    final Alert taskAlert = Alert.builder()
                            .type(id)
                            .businessOwner(taskInstance.getTask().getBusinessOwner())
                            .reference(Long.toString(planInstance.getId()))
                            .messageFields(fields).build();
                    final NotificationInstance notification = taskAlert.generateNotification(velocityUtil);
                    notification.setTaskInstance(taskInstance);
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

    public List<NotificationInstance> resume(PlanInstance planInstance) {
        long seconds = Duration.between(planInstance.getPauseStart().toInstant(), Instant.now()).getSeconds();
        long initialElapsedPauseTime = (planInstance.getElapsedPausedTime() != null ? planInstance.getElapsedPausedTime() : 0);
        planInstance.setElapsedPausedTime(seconds + initialElapsedPauseTime);
        planInstance.setEstimatedEnd(Date.from(planInstance.getEstimatedEnd().toInstant().plusSeconds(seconds)));
        for (TaskInstance taskInstance : planInstance.getTaskInstances()) {
            if (taskInstance.getStatus().equals(TaskStatus.PAUSED)) {
                taskInstance.setStatus(TaskStatus.IN_PROCESS);
                taskInstance.setEstimatedEnd(Date.from(taskInstance.getEstimatedEnd().toInstant().plusSeconds(seconds)));
            }
        }
        planInstance.setPauseStart(null);
        planInstance.setResumedTime(new Date());
        planInstance.setStatus(TaskStatus.IN_PROCESS);
        return generateNotificationsPlanPauseResume(planInstance, new VelocityUtil(), TaskStatus.IN_PROCESS);
    }

    private static boolean checkAllTasksFinished(HashMap<Integer, List<TaskInstance>> taskGroups) {
        for (Map.Entry<Integer, List<TaskInstance>> entry : taskGroups.entrySet()) {
            final List<TaskInstance> instances = entry.getValue();
            for (TaskInstance instance : instances) {
                if (!(TaskStatus.CANCELLED.equals(instance.getStatus()) || TaskStatus.COMPLETED.equals(instance.getStatus()))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkPreviousTaskGroupCompleted(HashMap<Integer, List<TaskInstance>> taskGroups, Integer currentTaskGroupOrder) {
        List<TaskInstance> previousTaskGroup = taskGroups.get(currentTaskGroupOrder);
        for (TaskInstance instance : previousTaskGroup) {
            switch (instance.getStatus()) {
                case PENDING_ACTION:
                case IN_PROCESS:
                    return false;
            }
        }
        return true;
    }

    private static void moveStatusesForward(List<TaskInstance> nextTaskGroup, Properties properties, JMSUtilContract jmsUtil) {
        if (!CollectionUtils.isEmpty(nextTaskGroup)) {
            for (TaskInstance instance : nextTaskGroup) {
                switch (instance.getStatus()) {
                    case PENDING_ACTION:
                        instance.setStatus(TaskStatus.IN_PROCESS);
                        instance.setLastUpdated(new Date());
                        if (instance.getTask().getTaskType().equals(TaskType.SYS)) {
                            try {
                                SystemTaskExecutor.execute(instance, properties, jmsUtil);
                                instance.setStatus(TaskStatus.COMPLETED);
                                instance.setActualEnd(new Date());
                                instance.setElapsedTime(Duration.between(instance.getStart().toInstant(), instance.getActualEnd().toInstant()).getSeconds());
                            } catch (JMSException e) {
                                instance.setStatus(TaskStatus.ERROR);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void generateNotifications(List<TaskInstance> nextTaskGroup, VelocityUtil velocityUtil) {
        if (!CollectionUtils.isEmpty(nextTaskGroup)) {
            for (TaskInstance instance : nextTaskGroup) {
                if (instance.getTask().getBusinessOwner() == null) {
                    continue;
                }

                Map<String, Object> values = new HashMap<>();
                values.put(NotificationDefinitionField.NOTIFICATION.getFieldName(), instance.getTask().getName());
                final Alert alert = Alert.builder()
                        .type(NotificationDefinitionId.NEW_TASK_TRIGGERED)
                        .businessOwner(instance.getTask().getBusinessOwner())
                        .messageFields(values).build();

                NotificationInstance notificationInstance = alert.generateNotification(velocityUtil);
                instance.addNotificationInstanceList(notificationInstance);
            }
        }
    }

    private static HashMap<Integer, List<TaskInstance>> orderTasksByGroup(List<TaskInstance> taskInstances) {
        HashMap<Integer, List<TaskInstance>> taskGroups = new HashMap<>(taskInstances.size());
        for (TaskInstance taskInstance : taskInstances) {
            final Integer key = taskInstance.getParsedOrder().getLeft();
            if (taskGroups.containsKey(key)) {
                taskGroups.get(key).add(taskInstance);
            } else {
                taskGroups.put(key, new ArrayList(Arrays.asList(taskInstance)));
            }
        }
        return taskGroups;
    }

    //The user should have been allowed to change those tasks with the status in progress.
    private static Pair<Integer, List<TaskInstance>> collectMinOrderWithStatusPresentNotNotified(HashMap<Integer, List<TaskInstance>> taskGroups, List<TaskStatus> statuses) {
        int minOrder = Integer.MAX_VALUE;
        List<TaskInstance> minOldStatusGroup = null;
        for (Map.Entry<Integer, List<TaskInstance>> entry : taskGroups.entrySet()) {
            if (minOrder > entry.getKey()) {
                if (existsStatus(entry.getValue(), statuses)) {
                    minOldStatusGroup = entry.getValue();
                    minOrder = entry.getKey();
                }
            }
        }
        if (minOldStatusGroup != null) {
            return ImmutablePair.of(minOrder, minOldStatusGroup);
        }
        return null;
    }

    private static boolean existsStatus(List<TaskInstance> taskInstances, List<TaskStatus> statuses) {
        for (TaskInstance instance : taskInstances) {
            if (statuses.contains(instance.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private static void setFirstTimeDates(PlanInstance planInstance) {
        LocalDateTime localEstimatedEnd = LocalDateTime.now(ZoneId.systemDefault());
        final List<TaskInstance> taskInstances = planInstance.getTaskInstances();
        if (!CollectionUtils.isEmpty(taskInstances)) {
            List<TaskInstance> orderedTasks = taskInstances.stream().sorted(Comparator.comparing(t -> t.getParsedOrder().getLeft())).collect(Collectors.toList());
            for (TaskInstance taskInstance : orderedTasks) {
                final Task task = taskInstance.getTask();
                final IntervalType durationMeasure = task.getDurationMeasure();
                final int durationValue = task.getDurationValue();
                switch (durationMeasure) {
                    case MINUTES:
                        localEstimatedEnd = localEstimatedEnd.plusMinutes(durationValue);
                        break;
                    case HOURS:
                        localEstimatedEnd = localEstimatedEnd.plusHours(durationValue);
                        break;
                    case DAYS:
                        localEstimatedEnd = localEstimatedEnd.plusDays(durationValue);
                        break;
                    case WEEKS:
                        localEstimatedEnd = localEstimatedEnd.plusWeeks(durationValue);
                        break;
                    default:
                        break;
                }
                taskInstance.setEstimatedEnd(Date.from(ZonedDateTime.of(localEstimatedEnd, ZoneId.systemDefault()).toInstant()));
            }
        }
        planInstance.setEstimatedEnd(Date.from(ZonedDateTime.of(localEstimatedEnd, ZoneId.systemDefault()).toInstant()));
    }

    public Map<NotificationDefinitionId, BusinessOwner> getNotificationDefinitionIdBusinessOwnerMap(List<NotificationDefinition> notificationDefinitions) {
        Map<NotificationDefinitionId, BusinessOwner> map = new HashMap();
        if (!CollectionUtils.isEmpty(notificationDefinitions)) {
            notificationDefinitions.forEach(nd -> map.put(nd.getType(), nd.getBusinessOwner()));
        }
        return map;
    }

    public void triggerNewProgramPlan(ProgramServiceAgreementEnrollment saEnrollment, WorkPlanType planType, WorkPlanServiceContract workPlanService, Properties properties, JMSUtilContract jmsUtil) throws BusinessException {
        final Program program = saEnrollment.getProgram();

        final WorkPlan workPlan = workPlanService.getByProgramAndWorkPlanType(program, planType);

        if (workPlan != null) {
            final PlanInstance planInstance = buildProgramPlanInstance(saEnrollment, workPlan, new VelocityUtil(), properties, jmsUtil);
            workPlanService.savePlanInstance(planInstance);
        } else {
            throw new BusinessException(BusinessException.ExceptionCode.RELATED_ENTITY_MISSING,
                    "Work plan not found for program {0} and type {1}.", Arrays.asList(program.getName(), planType.getName()));
        }
    }

    public void triggerNewRatePlan(RatePlanEnrollment enrollment, WorkPlanType planType, WorkPlanServiceContract workPlanService, Properties properties, JMSUtilContract jmsUtil) throws BusinessException {
        final RatePlan ratePlan = enrollment.getRatePlan();

        final WorkPlan workPlan = workPlanService.getByRatePlan(ratePlan, planType);

        if (workPlan != null) {
            final PlanInstance planInstance = buildRatePlanInstance(enrollment, workPlan, new VelocityUtil(), properties, jmsUtil);
            workPlanService.savePlanInstance(planInstance);
        } else {
            final RatePlanProfile activeProfile = ratePlan.getActiveProfile();
            String rateCodeId = StringUtils.EMPTY;
            if (activeProfile != null) {
                rateCodeId =activeProfile.buildRateCodeId();
            }
            throw new BusinessException(BusinessException.ExceptionCode.RELATED_ENTITY_MISSING,
                    "Work plan not found for rate plan {0} and type {1}.", Arrays.asList(rateCodeId, planType.getName()));
        }
    }

}
