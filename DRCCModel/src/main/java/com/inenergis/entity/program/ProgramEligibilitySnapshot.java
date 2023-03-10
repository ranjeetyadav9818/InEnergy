package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.model.EligibilityCheck;
import com.inenergis.model.EligibilityResult;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"application"})
@Entity
@Table(name = "PROGRAM_ELIGIBILITY_SNAPSHOT")
@EqualsAndHashCode(of = {"attribute","value","effectiveDate"})
public class ProgramEligibilitySnapshot extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID")
    private ProgramServiceAgreementEnrollment application;
    @Column(name = "ATTRIBUTE")
    private String attribute;
    @Column(name = "ELIGIBLE")
    private boolean eligible;
    @Column(name = "_VALUE")
    private String value;
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;
    @Column(name = "OVERRIDE_REASON")
    private String overrideReason;
    @Column(name = "OVERRIDE_USER")
    private String overrideUser;


    public static List<ProgramEligibilitySnapshot> createSnapshots(EligibilityResult eligible, ProgramServiceAgreementEnrollment enrollment, String overrideReason, String overrideUser){
        List<ProgramEligibilitySnapshot> result = new ArrayList<>();
        for (EligibilityCheck eligibilityCheck : eligible.getChecks()) {
            ProgramEligibilitySnapshot eligibilitySnapshot = new ProgramEligibilitySnapshot();
            eligibilitySnapshot.setApplication(enrollment);
            eligibilitySnapshot.setAttribute(eligibilityCheck.getAttribute());
            eligibilitySnapshot.setEffectiveDate(new Date());
            eligibilitySnapshot.setValue(eligibilityCheck.getValue());
            eligibilitySnapshot.setEligible(eligibilityCheck.isEligible());
            if (!eligibilityCheck.isEligible()) {
                eligibilitySnapshot.setOverrideReason(overrideReason);
                eligibilitySnapshot.setOverrideUser(overrideUser);
            }
            result.add(eligibilitySnapshot);
        }
        return result;
    }
}