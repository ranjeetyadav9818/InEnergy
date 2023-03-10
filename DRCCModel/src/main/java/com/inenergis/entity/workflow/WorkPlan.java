package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.WorkPlanType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.RatePlan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
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
@Table(name = "WORK_PLAN")
public class WorkPlan extends IdentifiableEntity {

	@Column(name = "TYPE", length = 5)
	@Enumerated(EnumType.STRING)
    private WorkPlanType type;
	
	@ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;
	@ManyToOne
    @JoinColumn(name = "RATE_PLAN_ID")
    private RatePlan ratePlan;
	
	@OneToMany(mappedBy = "workPlan", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List<WorkPlanTaskList> workPlanTaskLists;

}
