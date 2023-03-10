package com.inenergis.entity.program;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.workflow.RatePlanInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "RATE_PLAN_ENROLLMENT")
public class RatePlanEnrollment extends IdentifiableEntity {

    public static final String ENROLLED = "Enrolled";
    public static final String UNENROLLED = "Unenrolled";

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_ID")
    private RatePlan ratePlan;

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    private BaseServiceAgreement serviceAgreement;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @OneToMany(mappedBy = "ratePlanEnrollment", fetch = FetchType.LAZY)
    private List<RatePlanInstance> planInstances;

    public boolean isActive() {
        return endDate == null && ENROLLED.equalsIgnoreCase(status);
    }
}
