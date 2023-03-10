package com.inenergis.util;

import com.inenergis.entity.workflow.WorkPlanTaskList;

import java.util.Comparator;


public class WorkPlanTaskListComparator implements Comparator<WorkPlanTaskList> {

    @Override
    public int compare(WorkPlanTaskList taskList1, WorkPlanTaskList taskList2) {

        return taskList1.getOrder().compareTo(taskList2.getOrder());
    }

}
