package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "WORK_PLAN_TASK_LIST")
public class WorkPlanTaskList extends IdentifiableEntity {

	@Column(name = "TASK_ORDER", length = 20)
    private String order;
	
	@ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;
	
	@ManyToOne
    @JoinColumn(name = "WORK_PLAN_ID")
    private WorkPlan workPlan;

    
}
