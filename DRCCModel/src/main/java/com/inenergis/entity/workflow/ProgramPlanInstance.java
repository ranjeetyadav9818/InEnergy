package com.inenergis.entity.workflow;

import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@DiscriminatorValue("PROGRAM")
public class ProgramPlanInstance extends PlanInstance {

    @ManyToOne
    @JoinColumn(name = "PROGRAM_SA_ENROLLMENT_ID")
    private ProgramServiceAgreementEnrollment programServiceAgreementEnrollment;
}