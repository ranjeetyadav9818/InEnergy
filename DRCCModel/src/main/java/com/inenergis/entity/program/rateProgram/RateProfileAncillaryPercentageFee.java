package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.RatePercentageAncillaryApplicability;
import com.inenergis.entity.genericEnum.RatePercentageAncillaryType;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.RatePlanProfile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RATE_ANCILLARY_PERCENTAGE_FEE")
@Getter
@Setter
public class RateProfileAncillaryPercentageFee extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "DYNAMIC_FEE")
    private String dynamicFee;

    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "PERCENTAGE_TYPE")
    @Enumerated(EnumType.STRING)
    private RatePercentageAncillaryType type;

    @Column(name = "APPLICABILITY")
    @Enumerated(EnumType.STRING)
    private RatePercentageAncillaryApplicability applicability;

    @ManyToOne
    @JoinColumn(name = "SEASON_CALENDAR_ID")
    private TimeOfUseCalendar calendar;

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @ElementCollection
    @CollectionTable(name = "APPLICABLE_FEES", joinColumns = @JoinColumn(name = "ANCILLARY_PERCENTAGE_FEE_ID"))
    @Column(name="FEE")
    private List<String> applicableFees;


    @Transient
    private List<String> applicableFeesTransient;

    @PostLoad
    public void postLoad(){
        applicableFeesTransient = new ArrayList<>();
        applicableFeesTransient.addAll(applicableFees);
    }

    @Transient
    public boolean isEquivalentTo(RateProfileAncillaryPercentageFee firstFee) {
        return this.equals(firstFee) || ((this.getName() == null && firstFee.getName() == null || this.getName() != null && firstFee.getName() != null && this.getName().equals(firstFee.getName()))
                && (this.getCalendar() == null && firstFee.getCalendar() == null || this.getCalendar() != null && firstFee.getCalendar() != null && this.getCalendar().equals(firstFee.getCalendar()))
                && (this.getApplicability() == null && firstFee.getApplicability() == null || this.getApplicability() != null && firstFee.getApplicability() != null && this.getApplicability().equals(firstFee.getApplicability()))
                && (this.getType() == null && firstFee.getType() == null || this.getType() != null && firstFee.getType() != null && this.getType().equals(firstFee.getType())));

    }
}