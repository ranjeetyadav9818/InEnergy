package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.IntervalType;
import com.inenergis.entity.genericEnum.TaskType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TASK")
@EqualsAndHashCode(of = "name", callSuper = false)
public class Task extends IdentifiableEntity {

	@Column(name = "TASK_TYPE", length = 5)
	@Enumerated(EnumType.STRING)
    private TaskType taskType;
	
	@Column(name = "NAME", length = 255)
    private String name;
	
	@Column(name = "DESCRIPTION", length = 512)
    private String description;
	
	@Column(name = "DURATION_VALUE")
    private int durationValue;
	
	@Column(name = "DURATION_MEASURE", length = 20)
	@Enumerated(EnumType.STRING)
    private IntervalType durationMeasure;
	
	@ManyToOne
    @JoinColumn(name = "BUSINESS_OWNER_ID")
    private BusinessOwner businessOwner;
	
	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	private List<WorkPlanTaskList> workPlanTaskLists;
    
}
