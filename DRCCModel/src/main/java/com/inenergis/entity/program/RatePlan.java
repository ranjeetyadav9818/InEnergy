package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "RATE_PLAN")
public class RatePlan extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE_SECTOR")
    @Enumerated(EnumType.STRING)
    private RateCodeSector sector;

    @Column(name = "GAS_CODE_SECTOR")
    @Enumerated(EnumType.STRING)
    private GasRateCodeSector gasSector;


    @Column(name = "COMMODITY")
    @Enumerated(EnumType.STRING)
    private ComodityType commodityType;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RatePlanStatus status;

    @Column(name = "END_DATE")
    private Date endDate;

    @OneToMany(mappedBy = "ratePlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RatePlanProfile> profiles;

    @OneToMany(mappedBy = "ratePlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RatePlanEnrollment> enrollments;

    public void addProfile(RatePlanProfile newProfile) {
        if (profiles == null) {
            profiles = new ArrayList<>();
        }
        profiles.add(newProfile);
    }

    @Transient
    public RatePlanProfile getActiveProfile() {
        List<RatePlanProfile> profiles = getProfiles();
        if (profiles != null) {
            for (RatePlanProfile profile : getProfiles()) {
                Date now = new Date();
                if (now.after(profile.getEffectiveStartDate()) && (profile.getEffectiveEndDate() == null || now.before(profile.getEffectiveEndDate()))) {
                    return profile;
                }
            }
        }
        return null;
    }

    public boolean isActive() {
        return (endDate == null || endDate.after(new Date())) && RatePlanStatus.ACTIVE.equals(status);
    }
}
