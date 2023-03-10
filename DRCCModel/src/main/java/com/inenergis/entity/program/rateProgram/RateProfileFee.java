package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.RatePlanProfile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class RateProfileFee extends IdentifiableEntity implements Comparable {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "GROUP_ID")
    private Integer groupId;

    @ManyToOne
    @JoinColumn(name = "SEASON_CALENDAR_ID")
    private TimeOfUseCalendar calendar;

    @ManyToOne
    @JoinColumn(name = "RATE_TIER_ID")
    private RateTier rateTier;

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @ManyToOne
    @JoinColumn(name = "TOU_ID")
    private TimeOfUse timeOfUse;

    @Transient
    private final LocalDateTime creationTime = LocalDateTime.now();

    @Override
    public int compareTo(Object o) {
        RateProfileFee other = (RateProfileFee) o;
        if (this.getId() != null) {
            if (other.getId() != null) {
                return this.getId().compareTo(other.getId());
            } else {
                return 1; //I have id and the other not. I am superior
            }
        } else if (other.getId() != null) {
            return -1; //I do not have id. so I am inferior
        } else { // No ids to compare (both not persistent objects). Compare tiers
            if (this.getRateTier() == null && other.getRateTier() != null) {
                return this.getRateTier().compareTo(other.getRateTier());
            }
            return this.getCreationTime().compareTo(other.getCreationTime());
        }
    }

    @Transient
    public boolean shouldBeInTheSameGroup(RateProfileFee firstFee) {
        return (this.getName() == null && firstFee.getName() == null || this.getName() != null && firstFee.getName() != null && this.getName().equals(firstFee.getName()))
                && (this.getCalendar() == null && firstFee.getCalendar() == null || this.getCalendar() != null && firstFee.getCalendar() != null && this.getCalendar().equals(firstFee.getCalendar()))
                && (this.getTimeOfUse() == null && firstFee.getTimeOfUse() == null || this.getTimeOfUse() != null && firstFee.getTimeOfUse() != null && this.getTimeOfUse().equals(firstFee.getTimeOfUse()));

    }

}