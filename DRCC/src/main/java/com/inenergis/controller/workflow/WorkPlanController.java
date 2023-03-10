package com.inenergis.controller.workflow;


import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.entity.genericEnum.ProgramType;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.workflow.Task;
import com.inenergis.entity.workflow.WorkPlan;
import com.inenergis.entity.workflow.WorkPlanTaskList;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.TaskService;
import com.inenergis.service.WorkPlanService;
import com.inenergis.service.WorkPlanTaskListService;
import com.inenergis.util.UIMessage;
import com.inenergis.util.WorkPlanTaskListComparator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.event.DragDropEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class WorkPlanController implements Serializable {

    @Inject
    WorkPlanServiceContract workPlanService;

    @Inject
    private TaskService taskService;

    @Inject
    private ProgramServiceContract programService;

    @Inject
    private WorkPlanTaskListService workPlanTaskListService;

    @Inject
    private RatePlanService ratePlanServce;

    @Inject
    UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(WorkPlanController.class);

    private List<Program> programList;
    private List<RatePlan> ratePlanList;
    private RatePlan ratePlan;
    private Program program;
    private List<WorkPlanType> workPlanTypes;
    private WorkPlanType workPlanType;
    private List<Task> availableTasks;
    private WorkPlan workPlan;
    private ProgramType programType = ProgramType.DEMAND_RESPONSE;

    @PostConstruct
    public void init() {
        ratePlanList = ratePlanServce.getAll();
        programList = programService.getPrograms();
        workPlanTypes = Arrays.stream(WorkPlanType.values()).filter(v -> v.getProgramType().equals(programType)).collect(Collectors.toList());
        availableTasks = new ArrayList<>();
        workPlan = null;


    }

    public void search() {
        if (ProgramType.DEMAND_RESPONSE == programType) {


            workPlan = workPlanService.getByProgramAndWorkPlanType(program, workPlanType);




        } else {

            workPlan = workPlanService.getByRatePlan(ratePlan, workPlanType);

        }

        if (workPlan == null) {
            workPlan = new WorkPlan();
            workPlan.setProgram(program);
            workPlan.setRatePlan(ratePlan);
            workPlan.setType(workPlanType);
            workPlan.setWorkPlanTaskLists(new ArrayList<>());
        } else {


            Collections.sort(workPlan.getWorkPlanTaskLists(), new WorkPlanTaskListComparator());

        }
        this.loadAvailableTasks();

    }

    public void save() {
        if (!repeatedTaskNumbers(workPlan.getWorkPlanTaskLists(), uiMessage)) {
            workPlanService.save(workPlan);
            uiMessage.addMessage("Work plan saved");
            this.cancel();
        }
    }

    private boolean repeatedTaskNumbers(List<WorkPlanTaskList> workPlanTaskLists, UIMessage uiMessage) {
        if (!CollectionUtils.isEmpty(workPlanTaskLists)) {
            HashMap<BigDecimal, List<WorkPlanTaskList>> orders = new HashMap(workPlanTaskLists.size());
            for (WorkPlanTaskList task : workPlanTaskLists) {
                final String order = task.getOrder();
                if (NumberUtils.isNumber(order)) {
                    BigDecimal bdOrder = new BigDecimal(order);
                    if (orders.containsKey(bdOrder)) {
                        orders.get(bdOrder).add(task);
                    } else {
                        List<WorkPlanTaskList> newList = new ArrayList<>();
                        newList.add(task);
                        orders.put(bdOrder, newList);
                    }
                }
            }
            StringJoiner repeatedTasks = new StringJoiner(",");
            for (Map.Entry<BigDecimal, List<WorkPlanTaskList>> entry : orders.entrySet()) {
                final List<WorkPlanTaskList> tasks = entry.getValue();
                if (tasks.size() > 1) {
                    tasks.forEach(t -> repeatedTasks.add(t.getTask().getName()));
                    uiMessage.addMessage("The following Plan Tasks have the same order: {0}", repeatedTasks.toString(), FacesMessage.SEVERITY_ERROR);
                    return true;
                }
            }
        }
        return false;
    }

    public void cancel() {
        workPlan = null;
        this.search();

    }

    public void addAll() {
        Iterator<Task> taskIterator = this.availableTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            this.addWorkPlanTaskList(task);
        }
        this.availableTasks.clear();
    }

    public void removeAll() {
        workPlan.getWorkPlanTaskLists().clear();
        loadAvailableTasks();
    }

    public void remove(WorkPlanTaskList workPlanTaskList) {
        workPlan.getWorkPlanTaskLists().remove(workPlanTaskList);
        loadAvailableTasks();
    }

    public void onTaskDrop(DragDropEvent ddEvent) {
        Task task = ((Task) ddEvent.getData());
        this.addWorkPlanTaskList(task);
        this.availableTasks.remove(task);
    }

    private void addWorkPlanTaskList(Task task) {
        String nextOrder = calculateMNextOrder(workPlan.getWorkPlanTaskLists());
        WorkPlanTaskList wptl = new WorkPlanTaskList();
        wptl.setTask(task);
        task.getWorkPlanTaskLists().add(wptl);
        wptl.setWorkPlan(workPlan);
        workPlan.getWorkPlanTaskLists().add(wptl);
        wptl.setOrder(nextOrder);
    }

    private String calculateMNextOrder(List<WorkPlanTaskList> workPlanTaskLists) {
        int maxOrder = -1;
        if (!CollectionUtils.isEmpty(workPlanTaskLists)) {
            for (WorkPlanTaskList wpTask : workPlanTaskLists) {
                final String order = wpTask.getOrder();
                if (NumberUtils.isNumber(order)) {
                    int iOrder = Integer.parseInt(order.split("\\.")[0]);
                    if (iOrder > maxOrder) {
                        maxOrder = iOrder;
                    }
                }
            }
        }
        return Integer.toString(++maxOrder);
    }

    private void loadAvailableTasks() {
        availableTasks = taskService.getAll();
        if (!CollectionUtils.isEmpty(availableTasks)) {
            final List<WorkPlanTaskList> workPlanTaskLists = workPlan.getWorkPlanTaskLists();
            if (!CollectionUtils.isEmpty(workPlanTaskLists)) {
                final List<Task> workPlanTasks = workPlanTaskLists.stream().map(wptl -> wptl.getTask()).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(availableTasks)) {
                    availableTasks = availableTasks.stream().filter(a -> !workPlanTasks.contains(a)).collect(Collectors.toList());
                }
            }
        }
    }

    public void reOrder() {
        Collections.sort(workPlan.getWorkPlanTaskLists(), new WorkPlanTaskListComparator());
    }

    public void updateDropDownMenus() {init();}


}